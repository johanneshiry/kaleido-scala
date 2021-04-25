package plotly.kaleido

import com.typesafe.scalalogging.LazyLogging
import io.circe.parser.decode
import plotly.kaleido.KaleidoCmd.{KaleidoResult, KaleidoUp}

import java.io.{InputStream, OutputStream}
import java.util.concurrent.{LinkedBlockingQueue, TimeUnit}
import scala.sys.process.{ProcessIO, stringSeqToProcess}
import scala.util.{Failure, Success, Try}

case class KaleidoProcHandler private (kaleidoBin: String) extends LazyLogging {

  private val procOutQueue = new LinkedBlockingQueue[String](1)
  private val procInput = new java.io.PipedOutputStream()

  private val procStdInFn: OutputStream => Unit = { in: OutputStream =>
    val istream = new java.io.PipedInputStream(procInput)
    val buf = Array.fill(100)(0.toByte)
    var br = 0
    while (br >= 0) {
      br = istream.read(buf)
      if (br > 0) {
        in.write(buf, 0, br)
      }
    }
    in.close()
  }

  private val procStdOut: InputStream => Unit =
    out =>
      scala.io.Source
        .fromInputStream(out)
        .getLines()
        .map(msg => {
          procOutQueue.add(msg)
          msg
        })
        .foreach(msg => logger.error(msg))

  private val procErrOut: InputStream => Unit = { err =>
    scala.io.Source
      .fromInputStream(err)
      .getLines()
      .foreach(msg => logger.error(msg))
  }
  private val pio = new ProcessIO(procStdInFn, procStdOut, procErrOut)
  private val proc = Seq(kaleidoBin, "plotly", "--disable-gpu").run(pio)

  def generate(json: String): Try[KaleidoResult] = {
    if (!proc.isAlive()) {
      Failure(new RuntimeException("")) // todo
    } else {
      procInput.write(json.getBytes("UTF-8"))
      procInput.close()
      procOutQueue.poll(10, TimeUnit.SECONDS) match {
        case null => Failure(new RuntimeException("")) // todo
        case returnJsonString: String =>
          decode[KaleidoResult](returnJsonString) match {
            case Right(result) =>
              shutdown()
              Success(result)
            case Left(value) =>
              shutdown()
              Failure(value.fillInStackTrace()) // todo JH
          }
      }
    }
  }

  def shutdown(): Unit = {
    procOutQueue.clear()
    procInput.close()
    proc.destroy()
  }
}

object KaleidoProcHandler {
  def apply(kaleidoBin: String): Try[KaleidoProcHandler] = {
    val handler = new KaleidoProcHandler(kaleidoBin)
    handler.procOutQueue.poll(3, TimeUnit.SECONDS) match {
      case null => // kaleido startup failed
        handler.shutdown()
        Failure(throw new RuntimeException(s"Kaleido startup timeout!"))
      case startRes =>
        // startup successful?
        decode[KaleidoUp](startRes) match {
          case Right(handlerUp) if handlerUp.code == 0 =>
            Success(handler)
          case Left(value) =>
            handler.shutdown()
            Failure(value.fillInStackTrace())
          case Right(handlerUp) =>
            handler.shutdown()
            Failure(
              throw new RuntimeException(
                s"Error during Kaleido startup: $handlerUp"
              )
            )
        }
    }
  }
}
