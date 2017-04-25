package co.qeek.silverbars.liveordersboard.http

import co.qeek.silverbars.liveordersboard.domain.{Order, OrderType}
import co.qeek.silverbars.liveordersboard.services.{LiveOrdersBoardService, OrderNotFoundException}
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl._
import org.http4s.headers.Location

import scala.util.Try

object LiveOrdersBoardHttpService {
  def service: HttpService = {
    val board = new LiveOrdersBoardService
    HttpService {
      case GET -> Root / "summarised-orders" / orderTypeString =>
        OrderType.fromString(orderTypeString) match {
          case Some(orderType) =>
            Ok(board.summary(orderType).asJson)
          case None =>
            NotFound()
        }
      case req@POST -> Root / "orders" =>
        (for {
          order <- req.as(jsonOf[Order])
          resp <- board.registerOrder(order) match {
            case Left(_) =>
              BadRequest("Invalid order provided")
            case Right(orderId) =>
              Created("").putHeaders(Location(uri("orders") / orderId.toString))
          }
        } yield resp) handleWith {
          case _: InvalidMessageBodyFailure => BadRequest("Invalid message body provided")
        }
      case DELETE -> Root / "orders" / orderString =>
        (for {
          orderId <- Try(orderString.toLong).toEither
          result <- board.cancelOrder(orderId)
        } yield result) match {
          case Left(_: NumberFormatException) =>
            BadRequest("Invalid order id")
          case Left(OrderNotFoundException) =>
            NotFound()
          case Right(_) =>
            NoContent()
          case Left(_) =>
            InternalServerError("Unexpected error")
        }
    }
  }
}
