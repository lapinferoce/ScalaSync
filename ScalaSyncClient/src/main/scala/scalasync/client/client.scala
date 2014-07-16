package scalasync.client


import dispatch._

import scala.io._

import scalasync.core.{RepoFile, RepoClient}
/*
host("api.wunderground.com") / "api" / "5a7c66db0ba0323a" /
"conditions" / "q" / loc.state / (loc.city + ".xml")
  Http(weatherSvc(loc) OK as.xml.Elem).option

al result = Http(req OK as.String).either
And then use it like this, for example:

result() match {
  case Right(content)         => println("Content: " + content)
  case Left(StatusCode(404))  => println("Not found")
  case Left(StatusCode(code)) => println("Some other code: " + code.toString)
}

http://blog.knoldus.com/2013/07/30/integrating-google-drive-infrastructure-in-play-scala-application/

val post = :/("localhost") << Map(
  "commit" -> "true",
  ) << ("filename", new java.io.File("path/to/file"))

val http = new dispatch.Http
http(post >>> System.out)

  val post = server << Map(
    "id" -> "42",
  ) << ("name","filename", getStream)
 /*   val myRequest = url("http://127.0.0.1:8080/upload")
    myRequest.POST
    myRequest.setContentType("multipart/form-data","UTF8")
    //<< Map("filename" -> rf.sum)
    myRequest.addBodyPart(new FilePart("file",new java.io.File("/home/eric/devel/scalasync/ScalaSyncClient/testset3/ajax-loader.gif"))) /*<<  Map(
      "id" -> "42"
      ) << ("file", getStream(rf.indep_path))*/
    //val r =dispatch.Http(myRequest OK as.String).option
*/
*/
trait dispatchSupport {
  import scala.concurrent.ExecutionContext.Implicits.global

  def uploadFile(rf:RepoFile)={

    val local: Req = host("127.0.0.1", 8080)
    val encoded:Req = local.setContentType("multipart/form-data", "UTF-8").setHeader("Transfer-Encoding", "chunked").POST / "upload"
    val encoded2:Req  =encoded<<? Map("filename"->rf.sum)
    import com.ning.http.multipart.FilePart
    val filed: Req = encoded2.addBodyPart(new FilePart("file", new java.io.File(rf.indep_path)))

    val f = Http(filed)
    val c = f()

  }

  def getStream (x:String)=	new  java.io.FileInputStream(new java.io.File(x))


  def pushFile(rf:RepoFile)={
    val local: Req = host("127.0.0.1", 8080)
    val encoded:Req = local.setContentType("application/json","UTF-8").POST / "file"
    val filed:Req = encoded.setBody(rf.toJS)

    val f = Http(filed)
    val c = f()
  }


}
object Client extends App with dispatchSupport{

  println("yahoo")
  syncUp

  def syncUp ={
    val repo = RepoClient(new java.io.File("./testset3"))
    repo.scanFs()
    println("######> scan completed")
    val repo_old = RepoClient(new java.io.File("./testset3"))
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
            println("sending :"+fileToSend.sum+":"+fileToSend.filename)
            //clientActor ! doSend(fileToSend)
            uploadFile(fileToSend)
            println ("commiting " + fileToSend.sum+":"+fileToSend.filename)
            pushFile(fileToSend)
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
