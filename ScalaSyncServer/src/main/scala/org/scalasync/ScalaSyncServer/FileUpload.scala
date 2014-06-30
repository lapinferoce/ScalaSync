package org.scalasync.ScalaSyncServer

/**
 * Created by lapinferoce on 6/30/14.
 */
import org.scalatra._
import servlet.{MultipartConfig, SizeConstraintExceededException, FileUploadSupport}
import xml.Node
import java.nio.file.Files
import java.io.{File, FileOutputStream}

class FileUpload extends ScalatraServlet with FileUploadSupport with FlashMapSupport {
  configureMultipartHandling(MultipartConfig(maxFileSize = Some(3*1024*1024)))



  error {
    case e: SizeConstraintExceededException =>
      RequestEntityTooLarge(
        <p>The file you uploaded exceeded the 3 MB limit.</p>)
  }

  get("/upload") {
    contentType="text/html"

    <html>
      <head><title>Test</title></head>
      <body>
      <form action={url("/upload")} method="post" enctype="multipart/form-data">
        <p>File to upload: <input type="file" name="file" /></p>
        <p><input type="submit" value="Upload" /></p>
      </form>
        <p>
          Upload a file using the above form. After you hit "Upload"
          the file will be uploaded and your browser will start
          downloading it.
        </p>

        <p>
          The maximum file size accepted is 3 MB.
        </p>
      </body>
      </html>
  }

  /*

  * */
  post("/upload") {
    fileParams.get("file") match {
      case Some(file) =>
        /*val in=file.getInputStream
        val fout = new FileOutputStream(new File("/tmp/aa"));
        var c=1
        while ((c = in.read()) != -1) {
          fout.write(c);
        }
        if (in != null) {
                in.close();
        }
        if (fout != null) {
                fout.close();
        }*/
        val fout = new FileOutputStream(new File("/tmp/aa"))
        fout.write(file.get())
        Ok(file.get(), Map(
          "Content-Type"        -> (file.contentType.getOrElse("application/octet-stream")),
          "Content-Disposition" -> ("attachment; filename=\"" + file.name + "\"")
        ))

      case None =>
          <p>
            Hey! You forgot to select a file.
          </p>
    }
  }
}
