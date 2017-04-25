package co.qeek.silverbars.liveordersboard.http

import co.qeek.silverbars.liveordersboard.LiveOrdersBoardFixture
import co.qeek.silverbars.liveordersboard.domain.{OrderType, SummaryOrder}
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.dsl._
import org.http4s.client._
import org.http4s.circe._
import org.scalatest.{FlatSpec, Matchers}
import org.typelevel.scalatest.TaskValues

import scala.language.reflectiveCalls

class LiveOrdersBoardGetSummaryTest extends FlatSpec with TaskValues with Matchers {
  def fixture = new {
    val service: HttpService = LiveOrdersBoardHttpService.service
  }

  "GET /summarised-orders/buy" should "return summarised orders" in {
    val f = fixture
    val order = LiveOrdersBoardFixture.aValidOrder.copy(orderType = OrderType.BUY)
    val reg = f.service =<< POST(uri("/orders"), order.asJson)
    reg.runValue
    val res = f.service =<< GET(uri("/summarised-orders/buy"))
    res.runValue.status shouldBe Status.Ok
    res.runValue.as(jsonOf[List[SummaryOrder]]).runValue shouldBe
      List(SummaryOrder(order.quantityInKg, order.pricePerKg))
  }

  "GET /summarised-orders/sell" should "return summarised orders" in {
    val f = fixture
    val order = LiveOrdersBoardFixture.aValidOrder.copy(orderType = OrderType.SELL)
    val reg = f.service =<< POST(uri("/orders"), order.asJson)
    reg.runValue
    val res = f.service =<< GET(uri("/summarised-orders/sell"))
    res.runValue.status shouldBe Status.Ok
    res.runValue.as(jsonOf[List[SummaryOrder]]).runValue shouldBe
      List(SummaryOrder(order.quantityInKg, order.pricePerKg))
  }

  "GET /summarised-orders/sell" should "return an empty list if no orders" in {
    val f = fixture
    val res = f.service =<< GET(uri("/summarised-orders/sell"))
    res.runValue.status shouldBe Status.Ok
    res.runValue.as(jsonOf[List[SummaryOrder]]).runValue shouldBe List()
  }

  "GET /summarised-orders/asdf" should "return 404 not found" in {
    val f = fixture
    val res = f.service =<< GET(uri("/summarised-orders/asdf"))
    res.runValue.status shouldBe Status.NotFound
  }
}
