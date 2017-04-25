package co.qeek.silverbars.liveordersboard.services

import co.qeek.silverbars.liveordersboard.LiveOrdersBoardFixture
import org.scalatest.{FlatSpec, Matchers}

class LiveOrdersBoardRegisterOrderTest extends FlatSpec with Matchers {
  private val sut = new LiveOrdersBoardService()

  ".registerOrder" should "succeed for a valid Order" in {
    sut.registerOrder(LiveOrdersBoardFixture.aValidOrder) should be('right)
  }

  ".registerOrder" should "fail for invalid quantity" in {
    sut.registerOrder(LiveOrdersBoardFixture.aValidOrder.copy(quantityInKg = -1)) should be('left)
  }

  ".registerOrder" should "fail for invalid price" in {
    sut.registerOrder(LiveOrdersBoardFixture.aValidOrder.copy(pricePerKg = -1)) should be('left)
  }
}
