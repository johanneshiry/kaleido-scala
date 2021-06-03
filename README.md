# kaleido-scala

Scala bindings for plot.ly Kaleido

[![deploy CI](https://github.com/johanneshiry/kaleido-scala/actions/workflows/deploy.yml/badge.svg?branch=main)](https://github.com/johanneshiry/kaleido-scala/actions/workflows/deploy.yml)
[![Maven Central](https://img.shields.io/maven-central/v/org.plotly-scala/kaleido_2.13.svg)](https://maven-badges.herokuapp.com/maven-central/org.plotly-scala/kaleido_2.13)

*kaleido-scala* is a Scala library which provides static image (e.g. png, svg, pdf, etc.) generation capabilities
to [plotly-scala](http://plotly-scala.org/). It follows the same approach as the original
[Kaleido](https://github.com/plotly/Kaleido) python library and makes use of a standalone C++ application that embeds
the open-source Chromium browser as library. For more technical details refer to
[Kaleido approach](https://github.com/plotly/Kaleido#approach). For now scala 2.12 and 2.13 is supported.

This library is currently in an alpha stage and not considered to be production ready. However you are encouraged to
give it a try. Any feedback is highly welcome and it is intended to fix bugs as fast as possible. If you also have any
feature requests please do not hesitate and consider handing in an issue.

## Quick start

Add the latest version to your project. You can find an overview on all versions
[here](https://mvnrepository.com/artifact/org.plotly-scala/kaleido). Please do not just copy the string below, but adapt
the version number the desired one.

#### sbt

`libraryDependencies += "org.plotly-scala" %% "kaleido" % "0.1.0"`

#### gradle

`implementation group: 'org.plotly-scala', name: 'kaleido_2.13', version: '0.1.0'`

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
