package co.qeek.silverbars.liveordersboard.http

import co.qeek.silverbars.liveordersboard.LiveOrdersBoardFixture
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.Method.DELETE
import org.http4s._
import org.http4s.dsl._
import org.http4s.circe._
import org.http4s.client._
import org.http4s.headers.Location
import org.scalatest.{FlatSpec, Matchers, OptionValues}
import org.typelevel.scalatest.TaskValues

import scala.language.reflectiveCalls

class LiveOrdersBoardDeleteOrderTest extends FlatSpec with Matchers with TaskValues with OptionValues {
  def fixture = new {
    val service: HttpService = LiveOrdersBoardHttpService.service
  }

  "DELETE /orders/<nonexistent id>" should "return 404" in {
    val f = fixture
    val nonExistentOrderId = "1"
    val res = f.service =<< DELETE(uri("/orders") / nonExistentOrderId)
    res.runValue.status shouldBe Status.NotFound
  }

  "DELETE /orders/<registered id>" should "return 204" in {
    val f = fixture
    val t1 = f.service =<< POST(uri("/orders"), LiveOrdersBoardFixture.aValidOrder.asJson)
    val orderUri: Option[Location] = t1.runValue.headers.get(Location)

    val res = f.service =<< DELETE(orderUri.value.uri)
    res.runValue.status shouldBe Status.NoContent
  }

  "DELETE /orders/asdf" should "return 400" in {
    val f = fixture
    val res = f.service =<< DELETE(uri("/orders/asdf"))
    res.runValue.status shouldBe Status.BadRequest
  }
}
