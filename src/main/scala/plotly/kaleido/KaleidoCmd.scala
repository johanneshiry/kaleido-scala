package plotly.kaleido

import io.circe.{Decoder, HCursor}

object KaleidoCmd {

  implicit val decodeKaleidoResult: Decoder[KaleidoResult] = (c: HCursor) =>
    for {
      code <- c.downField("code").as[Int]
      message <- c.downField("message").as[Option[String]]
      pdfBgColor <- c.downField("pdfBgColor").as[Option[String]]
      format <- c.downField("format").as[Option[String]]
      result <- c.downField("result").as[String]
      width <- c.downField("width").as[Int]
      height <- c.downField("height").as[Int]
      scale <- c.downField("scale").as[Float]
    } yield KaleidoResult(
      code,
      message,
      pdfBgColor,
      KaleidoFormat(format.getOrElse("opsd")),
      result,
      width,
      height,
      scale
    )

  final case class KaleidoResult(
      code: Int,
      message: Option[String],
      pdfBgColor: Option[String],
      format: KaleidoFormat,
      result: String,
      width: Int,
      height: Int,
      scale: Float
  )

  implicit val decodeKaleidoUp: Decoder[KaleidoUp] = (c: HCursor) =>
    for {
      code <- c.downField("code").as[Int]
      message <- c.downField("message").as[Option[String]]
      version <- c.downField("version").as[String]
    } yield KaleidoUp(code, message, version)

  final case class KaleidoUp(
      code: Int,
      message: Option[String],
      version: String
  )

}
