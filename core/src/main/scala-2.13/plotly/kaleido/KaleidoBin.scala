package plotly.kaleido

import com.typesafe.scalalogging.LazyLogging

import java.io.{File, FileInputStream, FileOutputStream}
import java.net.URL
import java.nio.channels.Channels
import java.nio.file.Path
import java.util.zip.ZipInputStream
import scala.language.postfixOps
import scala.util.{Failure, Success, Try}

object KaleidoBin extends LazyLogging {

  // todo checksum test to validate downloaded file!

  // binary urls
  private val downloadFilename: String = s"kaleido_$os.zip"
  private val downloadUrl: URL = os match {
    case "win" =>
      new URL(
        "https://github.com/plotly/Kaleido/releases/download/v0.0.3/kaleido_win-0.0.3.zip"
      )
    case "linux" =>
      new URL(
        "https://github.com/plotly/Kaleido/releases/download/v0.0.3/kaleido_linux-0.0.3.zip"
      )
    case "mac" =>
      new URL(
        "https://github.com/plotly/Kaleido/releases/download/v0.0.3/kaleido_mac-0.0.3.zip"
      )
    case unsupported =>
      throw new IllegalArgumentException(
        s"Cannot determine binary download url for operation system: $unsupported"
      )
  }

  private val bin: String =
    os match {
      case "win"           => "kaleido.exe"
      case "linux" | "mac" => "kaleido"
      case unsupported =>
        throw new IllegalArgumentException(
          s"Cannot determine binary for operation system: $unsupported"
        )
    }

  private val basePath: String =
    path(System.getProperty("user.dir"), ".kaleidoCache")
  private val binaryPath: String = path(basePath, "kaleido", bin)

  def getOrDownload(): Try[String] = {
    // check if binary is available
    // if not we need to create the dir and download the required binary
    val path = if (!new File(binaryPath).exists()) {
      logger.info(s"Cannot find Kaleido binary in cache path '$binaryPath'.")
      createBasePath().map(download(_).flatMap(extract)) match {
        case Some(_: Success[Unit]) =>
          Success(binaryPath)
        case Some(fai: Failure[Unit]) =>
          Failure(fai.exception)
        case None =>
          Failure(
            new RuntimeException(
              s"Cannot create Kaleido cache directory '$basePath'!"
            )
          )
      }
    } else {
      Success(binaryPath)
    }

    //     set execution permission to binary, just to be sure
    path.map { bp =>
      // binary itself
      new File(bp).setExecutable(true, false)
      // kaleido/bin
      new File(this.path(basePath, "kaleido", "bin")).listFiles
        .map(_.setExecutable(true, false))
      bp
    }
  }

  private def download(basePath: String): Try[String] =
    Try {
      val downloadPath = path(basePath, downloadFilename)
      logger.info(
        s"Starting download from '$downloadUrl' to '$downloadPath'. This may take a while ..."
      )
      val rbc = Channels.newChannel(downloadUrl.openStream)
      val fos = new FileOutputStream(downloadPath)
      fos.getChannel.transferFrom(rbc, 0, Long.MaxValue)

      rbc.close()
      fos.close()
      logger.info("Download complete!")
      downloadPath
    }

  private def extract(filePath: String): Try[Unit] = Try {
    val zis = new ZipInputStream(new FileInputStream(filePath))
    LazyList.continually(zis.getNextEntry).takeWhile(_ != null).foreach {
      file =>
        if (!file.isDirectory) {
          val outPath = Path.of(basePath).resolve(file.getName)
          val outPathParent = outPath.getParent
          if (!outPathParent.toFile.exists()) {
            outPathParent.toFile.mkdirs()
          }
          val outFile = outPath.toFile
          val out = new FileOutputStream(outFile)
          val buffer = new Array[Byte](4096)
          LazyList
            .continually(zis.read(buffer))
            .takeWhile(_ != -1)
            .foreach(out.write(buffer, 0, _))
          out.close()
        }
    }
    zis.close()
  }

  private def createBasePath(): Option[String] =
    Option.when(new File(basePath).mkdirs())(basePath) match {
      case created @ Some(_) => created
      case None if new File(basePath).exists() =>
        Some(basePath)
      case _ => None
    }

  private def path(parts: String*): String =
    parts
      .foldLeft("")((res, cur) => res + File.separator + cur)
      .replace(File.separator + File.separator, File.separator)

  private def os: String = {
    val os = System.getProperty("os.name").toLowerCase
    if (os.contains("win"))
      "win"
    else if (os.contains("nix") || os.contains("nux") || os.contains("aix"))
      "linux"
    else if (os.contains("mac"))
      "mac"
    else
      throw new IllegalArgumentException(s"Unsupported operation system: $os")
  }

}
