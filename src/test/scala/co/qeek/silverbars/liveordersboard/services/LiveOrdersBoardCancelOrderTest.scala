package co.qeek.silverbars.liveordersboard.services

import co.qeek.silverbars.liveordersboard.LiveOrdersBoardFixture
import org.scalatest.{EitherValues, FlatSpec, Matchers}

class LiveOrdersBoardCancelOrderTest extends FlatSpec with Matchers with EitherValues {
  private val sut = new LiveOrdersBoardService()

  ".cancelOrder" should "leave no orders if only order cancelled" in {
    val someOrder = LiveOrdersBoardFixture.aValidOrder
    val orderId = sut.registerOrder(someOrder)
    val op = sut.cancelOrder(orderId.right.value)
    op should be('right)

    val summary = sut.summary(someOrder.orderType)
    summary should have size 0
  }

  ".cancelOrder" should "fail if order is not registered" in {
    val randomId = 38l
    val op = sut.cancelOrder(randomId)
    op should be('left)
  }
}
