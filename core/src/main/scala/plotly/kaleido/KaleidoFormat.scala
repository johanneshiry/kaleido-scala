package plotly.kaleido

sealed trait KaleidoFormat {
  val json: String
}

object KaleidoFormat {

  final case object PNG extends KaleidoFormat {
    val json: String = "png"
  }

  final case object SVG extends KaleidoFormat {
    val json: String = "svg"
  }

  final case object EPS extends KaleidoFormat {
    val json: String = "eps"
  }

  final case object PDF extends KaleidoFormat {
    val json: String = "pdf"
  }

  final case object EMF extends KaleidoFormat {
    val json: String = "emf"
  }

  def apply(format: String): KaleidoFormat = format match {
    case "png" => PNG
    case "svg" => SVG
    case "eps" => EPS
    case "pdf" => PDF
    case "emf" => EMF
    case unsupported =>
      throw new IllegalArgumentException(
        s"Unsupported output file format: $unsupported"
      )
  }

}
