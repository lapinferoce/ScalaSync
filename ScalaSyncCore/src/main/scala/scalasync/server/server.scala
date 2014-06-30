/*package scalasync.server

import akka.actor._
import com.typesafe.config.ConfigFactory
import java.io.{BufferedOutputStream, FileOutputStream}
import scalasync.core.{  DataChunk}
import scala.reflect.io.File

object Server extends App {

  val customConf = ConfigFactory.parseString( """
                                                |akka {
                                                |  actor {
                                                |    provider = "akka.remote.RemoteActorRefProvider"
                                                |  }
                                                |  remote {
                                                |    enabled-transports = ["akka.remote.netty.tcp"]
                                                |    netty.tcp {
                                                |      hostname = "127.0.0.1"
                                                |      port = 2552
                                                |    }
                                                |  }
                                                |}
                                              """.stripMargin)
  val system = ActorSystem("serverSystem", ConfigFactory.load(customConf))
  val serverActor = system.actorOf(Props(new ServerActor), name = "ServerActor")
}

class ServerActor extends Actor with mongoPersistances{
  def receive = {
    case data: DataChunk => {

      if (data.isLast){
        commitStore("/tmp/" + data.file.sum)
        addMetaData(data.file)
        //(File("/tmp/" + data.file.sum)).delete
        println("X")
        println("Delete"+"/tmp/" + data.file.sum)
      }
      else{
        //println("recving : /tmp/" + data.file.sum)
        val out = new FileOutputStream("/tmp/" + data.file.sum, true)

        //val bufferSize = 1024 * 1024
        val bis = new BufferedOutputStream(out)
        bis.write(data.bytesRead, 0, data.readsize)
        out.flush();
        out.close();
        print("#")
      }

    }
    /*case commit:CommitAllChunks =>{

    }*/
  }
}

//http://stackoverflow.com/questions/12366740/reading-file-contents-with-casbah-gridfs-throws-malformedinputexception

*/
