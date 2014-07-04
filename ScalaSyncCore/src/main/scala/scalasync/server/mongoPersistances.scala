package scalasync.core.server


import com.mongodb.casbah.Imports._
import com.mongodb.casbah.{MongoClient, MongoConnection}
import java.io.{InputStream, File, FileInputStream}
import com.mongodb.casbah.gridfs.GridFS
import scalasync.core.{RepoFileServer, RepoFile}
import com.mongodb.casbah.commons.MongoDBObject

/**
 * Created by lapinferoce on 6/26/14.
 */
object mongoPersistances {
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
/*
val query = MongoDBObject("platform" -> "JVM")
val update = $set("language" -> "Scala")
val result = coll.update( query, update )

println( "Number updated: " + result.getN )
for ( c <- coll.find ) println( c )
*/

 def findMetaBySum(asum:String):Option[RepoFileServer] = {
   val q= MongoDBObject("sum" -> asum)

   val mo  = for {
     x <- (mongoDBConnectionMeta.findOne(q))
   } yield x

   /*val w:Option[Int] = Some(1)
   val x:Option[Int] = Some(1)
   for {
     a<-w
     b<-x
   } yield a+b*/

   for {
     filename <- mo.get.getAs[String]("filename")
     indep_path <- mo.get.getAs[String]("indep_path")
     sum <- mo.get.getAs[String]("sum")
     deleted <- mo.get.getAs[Boolean]("deleted")
   }yield new RepoFileServer(filename,indep_path,sum,deleted)


 }

 def addMetaData(rf:RepoFileServer) = {
   val q = MongoDBObject("filename" -> rf.filename,
     "sum" -> rf.sum)
   val update = MongoDBObject("deleted" -> false,
     "timestamp" -> System.nanoTime())
   mongoDBConnectionMeta.update(q, update)
 }
}


/*
val mo = mongoDBConnectionMeta.findOne(q) match {
  case Some(m) =>
}

val m =  MongoDBObject("filename"->rf.filename,
                       "sum" -> rf.sum,
                       "deleted"-> false,
                       "timestamp" -> System.nanoTime())
mongoDBConnectionMeta += m*/

