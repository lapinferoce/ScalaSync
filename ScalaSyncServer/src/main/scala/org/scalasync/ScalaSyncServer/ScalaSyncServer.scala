package org.scalasync.ScalaSyncServer

import org.scalatra._
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.commands.JacksonJsonParsing
import java.io.{File, FileOutputStream}
import org.scalatra.servlet.FileUploadSupport

// JSON handling support from Scalatra
import org.scalatra.json._
import scalasync.core._



//         /->input                /->output
//with JacksonJsonParsing with JacksonJsonSupport
//https://github.com/scalatra/scalatra-website-examples/blob/master/2.2/formats/scalatra-commands/src/main/scala/org/scalatra/example/databinding/TodosController.scala

class ScalaSyncServer extends ScalasyncserverStack with FileUploadSupport with JacksonJsonSupport with JacksonJsonParsing {

  protected implicit val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  get("/") {
//    UploadItem(1, "bb")
  }

  get("/Ho") {
  //  UploadItem(2, "bb")
  }

/*  post("/uploadjs") {
    val data = parsedBody.extract[UploadItem]
    fileParams.get("file") match {
      case Some(file) =>
        val fout = new FileOutputStream(new File("/tmp/aa"))
        fout.write(file.get())
        Ok(file.get(), Map(
          "Content-Type" -> (file.contentType.getOrElse("application/octet-stream")),
          "Content-Disposition" -> ("attachment; filename=\"" + file.name + "\"")
        ))
      }
    data
  }*/

  post("/upload") {
    fileParams.get("file") match {
      case Some(file) =>
        val filename = params.getOrElse("filename","fail");
        println("writing:"+filename)
        val fout = new FileOutputStream(new File("/tmp/bin/"+filename))//new File("/tmp/"+file.getName).getCanonicalFile)
        fout.write(file.get())
        Ok("success")/*file.get(), Map(
          "Content-Type" -> (file.contentType.getOrElse("application/octet-stream")),
          "Content-Disposition" -> ("attachment; filename=\"" + file.name + "\"")
        ))*/
      case None => halt(500)
    }
  }
  post("/file") {
    val data = parsedBody.extract[RepoFile]
    println("registering:"+data.filename)
    Ok(data.toServer(false))
  }

/*  get("/file"){
    Ok("yes")
  }*/


}

//case class UploadItem(id: Int, name: String)
