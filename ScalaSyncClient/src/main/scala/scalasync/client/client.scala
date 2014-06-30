package scalasync.client

import java.io.File
import scalasync.core.RepoClient


object Client extends App{

  println("yahoo")
  def syncUp ={
    val repo = RepoClient(new File("./testset3"))
    repo.scanFs()
    println("######> scan completed")
    val repo_old = RepoClient(new File("./testset3"))
    repo_old.unSerializeFrom("/tmp/old.js")
    println("######> got old repo")
    val changeset =repo.diff(repo_old)
    println("######> changeset computed")
    changeset match {
      case (added:Set[String],deleted:Set[String]) => {
        println("nb file added  :"+added.size)
        println("nb file deleted:"+deleted.size)
        added.foreach{id=>
          for(fileToSend <- repo.map.get(id))
          {
            println("sending :"+fileToSend.filename+":"+fileToSend.sum)
            //clientActor ! doSend(fileToSend)
          }

        }
        deleted.foreach{id=>
          for(fileToDelete <- repo.map.get(id))
          {
            println("deleting :"+fileToDelete.filename)
            //clientActor ! doSend(fileToDelete)
          }
        }
      }
      case _ => println("nothing to do")
    }

  }
}
/*package scalasync.client

import akka.actor._
import com.typesafe.config.ConfigFactory
import java.io.{File, FileInputStream}
import scalasync.core._
import scalasync.client.doSend
import scalasync.core.RepoFile
import scalasync.core.DataChunk
import scalasync.core.RepoClient

object Client extends App {

  val customConf = ConfigFactory.parseString("""
akka {
                                               |
                                               |  actor {
                                               |    provider = "akka.remote.RemoteActorRefProvider"
                                               |  }
                                               |  remote {
                                               |    enabled-transports = ["akka.remote.netty.tcp"]
                                               |    netty.tcp {
                                               |      hostname = "127.0.0.1"
                                               |      port = 2553
                                               |    }
                                               |  }
                                               |}
                                               |
                                               |
                                             """.stripMargin)
  val system = ActorSystem("clientSystem",ConfigFactory.load(customConf))
  val clientActor = system.actorOf(Props(new ClientActor), name = "ClientActor")

  syncUp
  syncDown



  def syncDown={

  }

}

class ClientActor extends Actor {
  private val server = context.actorSelection("akka.tcp://serverSystem@127.0.0.1:2552/user/ServerActor")

  def receive = {
    case toSend:doSend    => {
      val filename = toSend.playload.indep_path //new File("/home/lapinferoce/devel/scala/ScalaSync/testset3/Wargames.AVI")
      val is = new FileInputStream(filename)
      val bufferSize = 120000
      val bb = new Array[Byte](bufferSize)
      val bis = new java.io.BufferedInputStream(is)
      var bytesRead = bis.read(bb, 0, bufferSize)

      while (bytesRead > 0) {
        server ! DataChunk(toSend.playload,bb,bytesRead,false)
        //md.update(bb,0,bytesRead)
        bytesRead = bis.read(bb, 0, bufferSize)
        print("#")
      }
      bis.close()
      server ! DataChunk(toSend.playload,bb,0,true)
      println("X")
    }


//    case message: String => println("Client received reply: " + message)
    case _ => println("I got something I didn't understand.")
  }
}


case object SayHello
case class doSend(playload:RepoFile)
case class doDelete(playload:RepoFile)

*/