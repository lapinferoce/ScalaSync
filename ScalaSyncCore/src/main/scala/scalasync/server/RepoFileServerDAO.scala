package scalasync.server

import com.mongodb.casbah.MongoConnection
import com.novus.salat._
import com.novus.salat.dao.SalatDAO
import com.novus.salat.global._
import org.bson.types.ObjectId

import scalasync.core.RepoFileServer

/**
 * Created by eric on 7/7/14.
 */
object RepoFileServerDAO extends SalatDAO[RepoFileServer, ObjectId](
   collection = MongoConnection()("ScalaSync")("meta")
)

/*
val mongoDBConnectionBin = MongoConnection()("ScalaSync")
val gridFS = GridFS(mongoDBConnectionBin)
val mongoDBConnectionMeta = mongoDBConnectionBin("meta")


def commitStore(filename:String) = {
val file = new File(filename)
val fileInputStream=new FileInputStream(file)
val gfsFile = gridFS.createFile(fileInputStream)
gfsFile.filename=filename
gfsFile.save
}*/


