package scalasync.core

import java.io.File

/**
 * Created by lapinferoce on 6/22/14.
 */

/*case class RepoFile(filename:String,original_path:String,sum:String) extends AbstractRepoFile
case class RepoFileWithOutSum(filename:String,original_path:String) extends AbstractRepoFile  {
  val sum = fileUtil.computeSum(original_path, original_path)
}*/


case class RepoFile(filename:String,indep_path:String,sum:String){

  def isSumValid(sum:String):Boolean = (computeSum==sum)


  def computeSum():RepoFile={
    new RepoFile(filename,indep_path,fileUtil.computeSum(indep_path, filename))
  }

  def dump():String={
    "["+filename+"|"+indep_path+"|"+sum+"]"
  }
}

/*
  def computeSum():RepoFile={

    val sum = fileUtil.computeSum(original_path,original_path)
    new RepoFile(filename,original_path,sum)
  }
 */