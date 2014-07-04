package scalasync.core

//import java.io.File
import scala.collection.immutable.HashMap


import org.json4s._
import org.json4s.jackson.JsonMethods._

import scalax.io._
/**
 * Created by lapinferoce on 6/22/14.
 */
case class RepoClient(repoDir:java.io.File) extends Repo {
  path = repoDir.getCanonicalPath

  var map     = new HashMap[String,RepoFile]()

  implicit val formats = DefaultFormats

  def scanFs():Array[RepoFile] = {
    val files = fsCrawler.find_file_recursively(repoDir).map(x=> mkRepoFileFromFile(x))
    files foreach { rf => map ++= Map(rf.sum-> rf) }
    files
  }

  def mkRepoFileFromFile(f:java.io.File):RepoFile={
    val fullpath = f.getCanonicalPath
    val filename = removeRoot(fullpath)
    (new RepoFile(filename,fullpath,null)).computeSum
  }

  def dump():String=map.foldLeft("")( (acc, kv) => acc + kv._1+"\n\t"+ kv._2.dump()+"\n")
    //(acc,(k,v:RepoFile)) => acc+k+":"+v.dump}



  // stuff
  def diff(that:RepoClient):(Set[String],Set[String])= {
    val added = map.keySet.diff(that.map.keySet)
    val deleted = that.map.keySet.diff(map.keySet)
    (added,deleted)
  }
    //val addedkeys = map.keySet.diff(that.map.keySet) //map.keySet.intersect(that.map.keySet)
    //val removedkeys = that.map.keySet.diff(map.keySet)
  // serialisation / unserialisation

  def toJS():String={
   compact(render(Extraction.decompose(map.values.toArray)))
  }

  def fromJS(s:String):Unit={
    val js=parse(s)
    val arr= js.extract[Array[RepoFile]]
    arr foreach { rf => map++=Map(rf.sum-> rf) }
  }
  def unSerializeFrom(from:String):Unit={
    if((new java.io.File(from).exists))
        fromJS(fileUtil.readFromFile(from))
  }

  def serializeTo(to:String):Unit={
    fileUtil.writeToFile(to,toJS)
  }
}
