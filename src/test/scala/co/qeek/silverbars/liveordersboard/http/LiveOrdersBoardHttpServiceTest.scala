package co.qeek.silverbars.liveordersboard.http

import org.http4s._
import org.http4s.dsl._
import org.http4s.client._
import org.scalatest.{FlatSpec, Matchers}
import org.typelevel.scalatest.TaskValues

class LiveOrdersBoardHttpServiceTest extends FlatSpec with Matchers with TaskValues {
  val service: HttpService = LiveOrdersBoardHttpService.service

  "LiveOrdersBoardHttpService" should "not return 404 from /summarised-orders/sell" in {
    val res = service =<< GET(uri("/summarised-orders/sell"))
    res.runValue.status should not be Status.NotFound
  }

  it should "not return 404 from /summarised-orders/buy" in {
    val res = service =<< GET(uri("/summarised-orders/buy"))
    res.runValue.status should not be Status.NotFound
  }
}
