package scalasync.core

import java.io.File

/**
 * Created by lapinferoce on 6/22/14.
 */
object fsCrawler{

  def find_file_recursively(f:File):Array[File]= (f.exists&&f.isFile) match {
    case true   =>  Array(f)
    case false  =>{
      val  these = (f.listFiles).filter(_.canRead)
      these.filter(_.isFile)++these.filter(_.isDirectory).flatMap(find_file_recursively )
    }
  }
}
