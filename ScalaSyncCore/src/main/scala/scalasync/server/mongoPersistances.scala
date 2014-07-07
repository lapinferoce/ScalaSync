package scalasync.core.server


import com.mongodb.casbah.Imports._
import com.mongodb.casbah.{MongoClient, MongoConnection}
import java.io.{InputStream, File, FileInputStream}
import com.mongodb.casbah.gridfs.GridFS
import scala.reflect.io.File
import scalasync.core.{RepoFileServer, RepoFile}
import com.mongodb.casbah.commons.MongoDBObject

import scalasync.server.RepoFileServerDAO

/**
 * Created by lapinferoce on 6/26/14.
 * http://repo.novus.com/salat-presentation/#13
 */
object mongoPersistances {
  val mongoDBConnectionBin = MongoConnection()("ScalaSync")
  val gridFS = GridFS(mongoDBConnectionBin)
  val mongoDBConnectionMeta = mongoDBConnectionBin("meta")


  def findMetaBySum(sum: String): Option[RepoFileServer] = {
    RepoFileServerDAO.findOne(MongoDBObject("sum" -> sum))
  }

  def addMetaData(rf: RepoFileServer) {
    //rf.deleted(true)
    RepoFileServerDAO.update(MongoDBObject("sum" -> rf.sum), RepoFileServerDAO.toDBObject(rf), true)
  }

  def deleteMetaData(rf: RepoFileServer) {
    //rf.deleted(false)
    RepoFileServerDAO.update(MongoDBObject("sum" -> rf.sum), RepoFileServerDAO.toDBObject(rf), false)
  }

  def commitStore(filename: String) = {
    val file = new java.io.File(filename)
    val fileInputStream = new FileInputStream(file)
    val gfsFile = gridFS.createFile(fileInputStream)
    gfsFile.filename = filename
    gfsFile.save
  }
}
