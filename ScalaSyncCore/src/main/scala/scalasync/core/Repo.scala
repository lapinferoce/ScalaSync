package scalasync.core

import java.io.File

/**
 * Created by lapinferoce on 6/26/14.
 */
trait Repo {
  var path: String = _

  def removeRoot(apath:String):String = {
    new File(apath).getCanonicalPath.replace(path,"")
  }


  def restoreRoot(apath:String):String = {
    if(apath.startsWith("/")||path.endsWith("/")) (new File(path+apath)).getCanonicalPath
    else (new File(path+File.separatorChar+apath)).getCanonicalPath
  }
}
