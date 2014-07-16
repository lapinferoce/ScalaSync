package scalasync.core

import java.io.File

import org.json4s._
import org.json4s.jackson.JsonMethods._

/**
 * Created by lapinferoce on 6/22/14.
 */

/*case class RepoFile(filename:String,original_path:String,sum:String) extends AbstractRepoFile
case class RepoFileWithOutSum(filename:String,original_path:String) extends AbstractRepoFile  {
  val sum = fileUtil.computeSum(original_path, original_path)
}*/

trait RepoFileInterface{
  def dump():String

}
case class RepoFile(filename:String,indep_path:String,sum:String) extends RepoFileInterface{
  implicit val formats = DefaultFormats

  def isSumValid(sum:String):Boolean = (computeSum==sum)


  def computeSum():RepoFile={
    new RepoFile(filename,indep_path,fileUtil.computeSum(indep_path, filename))
  }

  def dump():String={
    "["+filename+"|"+indep_path+"|"+sum+"]"
  }
  def toJS():String={
    compact(render(Extraction.decompose(this)))
  }

  def toServer(deleted:Boolean):RepoFileServer = RepoFileServer(filename,indep_path,sum,deleted)
}

case class RepoFileServer(filename:String,indep_path:String,sum:String,deleted:Boolean) extends RepoFileInterface{
  def dump():String={
    "["+filename+"|"+indep_path+"|"+sum+"|"+(if (deleted){"deleted"}else{"exist"})+"]"
  }

  def toClient():RepoFile = RepoFile(filename,indep_path,sum)
}
/*
  def computeSum():RepoFile={

    val sum = fileUtil.computeSum(original_path,original_path)
    new RepoFile(filename,original_path,sum)
  }
 */