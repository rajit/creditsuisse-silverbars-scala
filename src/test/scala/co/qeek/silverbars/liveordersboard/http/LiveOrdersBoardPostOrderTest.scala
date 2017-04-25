package co.qeek.silverbars.liveordersboard.http

import co.qeek.silverbars.liveordersboard.LiveOrdersBoardFixture
import io.circe.generic.auto._
import io.circe.literal._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.client._
import org.http4s.dsl._
import org.scalatest.{FlatSpec, Matchers}
import org.typelevel.scalatest.TaskValues

class LiveOrdersBoardPostOrderTest extends FlatSpec with Matchers with TaskValues {
  val service: HttpService = LiveOrdersBoardHttpService.service

  "POST /orders" should "return 400 for incorrect JSON" in {
    val res = service =<< POST(uri("/orders"), json"""{}""")
    assert(res.runValue.status == Status.BadRequest)
  }

  it should "return 201 for a valid order" in {
    val res = service =<< POST(uri("/orders"), LiveOrdersBoardFixture.aValidOrder.asJson)
    assert(res.runValue.status == Status.Created)
  }

  it should "return 400 for an invalid order" in {
    val res = service =<< POST(uri("/orders"), LiveOrdersBoardFixture.aValidOrder.copy(quantityInKg = -1).asJson)
    assert(res.runValue.status == Status.BadRequest)
  }
}
