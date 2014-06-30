package scalasync.core

import java.io.{FileInputStream, FileOutputStream, File}
import java.security.MessageDigest
import java.util.Scanner

/**
 * Created by lapinferoce on 6/22/14.
 */
object fileUtil {
 /* def cp(src:String,dest:String){
    val srcFile = new File(src)
    val destFile = new File(dest)
    new FileOutputStream(destFile).getChannel() transferFrom(
      new FileInputStream(srcFile).getChannel(), 0, Long.MaxValue )
  }*/
  def computeSum(filename:String,rest:String):String={
    val restfile = fileUtil.abstractPath(rest)
    //println(">> Filename:"+filename)
    val md = MessageDigest.getInstance("SHA-1")

    val is = new FileInputStream(filename);
    val bufferSize = 1024 * 1024
    val bb = new Array[Byte](bufferSize)
    val bis = new java.io.BufferedInputStream(is)
    var bytesRead = bis.read(bb, 0, bufferSize)

    while (bytesRead > 0) {
      md.update(bb,0,bytesRead)
      bytesRead = bis.read(bb, 0, bufferSize)
    }
    md.update(restfile.getBytes,0,restfile.getBytes.length)

    md.digest().map("%02x".format(_)).mkString
  }
  def writeToFile(p: String, s: String)= {
    val pw = new java.io.PrintWriter(new File(p))
    try {
      pw.write(s)
    } finally {
      pw.close()
    }
  }
  def readFromFile(p: String):String= {
    new Scanner(new File(p)).useDelimiter("\\Z").next();
  }
  // convert to os independent file path separator
  def abstractPath(p:String ):String = {
    p.replace(File.separator,"/")
  }
  // convert to os dependent file path separator
  def realPath(p:String):String={
    p.replace("/",File.separator)
  }
}
