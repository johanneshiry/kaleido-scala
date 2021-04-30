package plotly.kaleido

final case class KaleidoPlot(
    data: String,
    format: KaleidoFormat,
    width: Int,
    height: Int,
    scale: Float
) {

  def asJsonString(): String =
    s"""{
       |"data" : $data,
       |"format" : "${format.json}",
       |"width" : $width,
       |"height" : $height,
       |"scale" : $scale
       |}
       |""".stripMargin.replace("\n", "").replace("\\", "").replace(" ", "")

}
