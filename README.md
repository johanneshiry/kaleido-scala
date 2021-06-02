# kaleido-scala

Scala bindings for plot.ly Kaleido

[![Release CI](https://github.com/johanneshiry/kaleido-scala/actions/workflows/deploy.yml/badge.svg)](https://github.com/johanneshiry/kaleido-scala/actions/workflows/release.yml)

*kaleido-scala* is a Scala library which provides static image (e.g. png, svg, pdf, etc.) generation capabilities
to [plotly-scala](http://plotly-scala.org/). It follows the same approach as the original
[Kaleido](https://github.com/plotly/Kaleido) python library and makes use of a standalone C++ application that embeds
the open-source Chromium browser as library. For more technical details refer to
[Kaleido Approach](https://github.com/plotly/Kaleido#approach).

## Quick start

Add the latest version to your project:

#### sbt

#### gradle

### Usage

```scala 
import plotly.kaleido.Kaleido._ // adds save(...) method to plotly-scala

val x = (0 to 100).map(_ * 0.1)
val y1 = x.map(d => 2.0 * d + util.Random.nextGaussian())
val y2 = x.map(math.exp)

val plot = Seq(
  Scatter(x, y1).withName("Approx twice"),
  Scatter(x, y2).withName("Exp")
)

val layout = Layout().withTitle("Curves")
val height = 400
val width = 400 
val scale = 1

plot.save("/desired/output/path", "filename", layout, PDF, height, width, scale)

```

As the library makes use of an underlying C++ application, it tries to download the required binary for you
first `save(...)` call. If you prefer to provide it manually you can download it from the official
[Kaleido repo](https://github.com/plotly/Kaleido/releases)
and provide the path to the downloaded binary when you call `save(...)`.