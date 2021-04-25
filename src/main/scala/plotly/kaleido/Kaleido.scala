package plotly.kaleido

import plotly.kaleido.KaleidoFormat._
import plotly.{Config, Plotly, Trace}
import plotly.layout.Layout

import java.io.{BufferedOutputStream, File, FileOutputStream}
import java.nio.file.Path
import java.util.Base64
import scala.util.{Success, Try}

object Kaleido {

  val kaleidoBin =
    "/Users/jhiry/IdeaProjects/00_Tonne/kaleido-scala/tmp/0.0.3/kaleido" // todo download on request

  implicit class KaleidoOps(val trace: Seq[Trace]) extends AnyVal {
    def save(
        outputPath: String,
        fileName: String,
        layout: Layout,
        format: KaleidoFormat,
        width: Int,
        height: Int,
        scale: Float
    ): Try[Unit] =
      Kaleido.save(
        outputPath,
        fileName,
        Plotly.jsonSnippet(trace, layout, Config()),
        format,
        width,
        height,
        scale
      )
  }

  def save(
      outputPath: String,
      fileName: String,
      jsonData: String,
      format: KaleidoFormat,
      width: Int,
      height: Int,
      scale: Float
  ): Try[Unit] = {
    val plot =
      KaleidoPlot(jsonData, format, width, height, scale).asJsonString()
    KaleidoProcHandler(kaleidoBin)
      .flatMap(_.generate(plot))
      .flatMap(
        kaleidoResult => {
          val outputFilePath = outputFile(outputPath, fileName, format)
          kaleidoResult.format match {
            case SVG | EPS =>
              writeFile(kaleidoResult.result.getBytes, outputFilePath)
            case _ =>
              writeFile(
                Base64.getDecoder.decode(kaleidoResult.result),
                outputFilePath
              )
          }
        }
      )
  }

  private def outputFile(
      path: String,
      filename: String,
      format: KaleidoFormat
  ): Path =
    new File(path + File.separator + filename + "." + format.json).toPath.toAbsolutePath

  private def writeFile(
      file: Array[Byte],
      outputFilePath: Path
  ): Success[Unit] = {
    val bos = new BufferedOutputStream(
      new FileOutputStream(outputFilePath.toString)
    )
    bos.write(file)
    Success(bos.close())
  }
}
