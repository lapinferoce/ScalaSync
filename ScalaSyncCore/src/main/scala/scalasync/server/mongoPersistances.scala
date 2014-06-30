package scalasync.server



import com.mongodb.casbah.{MongoClient, MongoConnection}
import java.io.{InputStream, File, FileInputStream}
import com.mongodb.casbah.gridfs.GridFS
import scalasync.core.RepoFile
import com.mongodb.casbah.commons.MongoDBObject

/**
 * Created by lapinferoce on 6/26/14.
 */
trait mongoPersistances {
  val mongoDBConnectionBin = MongoConnection()("ScalaSync")
  val gridFS = GridFS(mongoDBConnectionBin)
  val mongoDBConnectionMeta = mongoDBConnectionBin("meta")


  def commitStore(filename:String) = {
    val file = new File(filename)
    val fileInputStream=new FileInputStream(file)
    val gfsFile = gridFS.createFile(fileInputStream)
    gfsFile.filename=filename
    gfsFile.save
  }

 def addMetaData(rf:RepoFile) = {
   val m =  MongoDBObject("filename"->rf.filename,
                          "sum" -> rf.sum,
                          "deleted"-> false,
                          "timestamp" -> System.nanoTime())
   mongoDBConnectionMeta += m
 }



}
