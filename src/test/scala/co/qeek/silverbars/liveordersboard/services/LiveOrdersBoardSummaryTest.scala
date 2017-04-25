package co.qeek.silverbars.liveordersboard.services

import co.qeek.silverbars.liveordersboard.LiveOrdersBoardFixture
import co.qeek.silverbars.liveordersboard.domain.{Order, OrderType, SummaryOrder}
import org.scalatest.{FlatSpec, Matchers}

import scala.language.reflectiveCalls

class LiveOrdersBoardSummaryTest extends FlatSpec with Matchers {
  val aValidOrder: Order = LiveOrdersBoardFixture.aValidOrder

  def fixture = new {
    val service = new LiveOrdersBoardService
  }

  ".summary(BUY)" should "show single order when one order registered" in {
    val f = fixture
    f.service.registerOrder(aValidOrder)
    val summary = f.service.summary(OrderType.BUY)
    summary should have size 1
    summary should contain(SummaryOrder(quantityInKg = aValidOrder.quantityInKg, pricePerKg = aValidOrder.pricePerKg))
  }

  it should "show two orders when two orders registered" in {
    val f = fixture
    f.service.registerOrder(aValidOrder)
    f.service.registerOrder(aValidOrder.copy(pricePerKg = 295))
    val summary = f.service.summary(OrderType.BUY)
    summary should have size 2
    summary should contain(SummaryOrder(quantityInKg = aValidOrder.quantityInKg, pricePerKg = aValidOrder.pricePerKg))
    summary should contain(SummaryOrder(quantityInKg = aValidOrder.quantityInKg, pricePerKg = 295))
  }

  it should "summarise two orders with the same price in one" in {
    val f = fixture
    f.service.registerOrder(aValidOrder.copy(quantityInKg = 5))
    f.service.registerOrder(aValidOrder.copy(quantityInKg = 6))
    val summary = f.service.summary(OrderType.BUY)
    summary should have size 1
    summary.head should have('quantityInKg (11))
  }

  it should "only show BUY orders" in {
    val f = fixture
    f.service.registerOrder(aValidOrder.copy(pricePerKg = 1919))
    f.service.registerOrder(aValidOrder.copy(pricePerKg = 2323, orderType = OrderType.SELL))
    val summary = f.service.summary(OrderType.BUY)
    summary should have size 1
    summary.head should have('pricePerKg (1919))
  }

  it should "sort orders highest price first" in {
    val f = fixture
    val buyOrder = aValidOrder.copy(orderType = OrderType.BUY)
    f.service.registerOrder(buyOrder.copy(pricePerKg = 1))
    f.service.registerOrder(buyOrder.copy(pricePerKg = 2))
    f.service.registerOrder(buyOrder.copy(pricePerKg = 3))
    val summary = f.service.summary(OrderType.BUY)
    summary should have size 3
    summary.head should have ('pricePerKg (3))
    summary(1) should have ('pricePerKg (2))
    summary(2) should have ('pricePerKg (1))
  }

  ".summary(SELL)" should "only show SELL orders" in {
    val f = fixture
    f.service.registerOrder(aValidOrder.copy(pricePerKg = 1919))
    f.service.registerOrder(aValidOrder.copy(pricePerKg = 2323, orderType = OrderType.SELL))
    val summary = f.service.summary(OrderType.SELL)
    summary should have size 1
    summary.head should have('pricePerKg (2323))
  }

  it should "sort orders highest price first" in {
    val f = fixture
    val buyOrder = aValidOrder.copy(orderType = OrderType.SELL)
    f.service.registerOrder(buyOrder.copy(pricePerKg = 1))
    f.service.registerOrder(buyOrder.copy(pricePerKg = 2))
    f.service.registerOrder(buyOrder.copy(pricePerKg = 3))
    val summary = f.service.summary(OrderType.SELL)
    summary should have size 3
    summary.head should have ('pricePerKg (1))
    summary(1) should have ('pricePerKg (2))
    summary(2) should have ('pricePerKg (3))
  }

  ".sumQuantityInKg" should "use same price as given order" in {
    val f = fixture
    val order = f.service.sumQuantityInKg(List(SummaryOrder.from(aValidOrder.copy(pricePerKg = 17))))
    order.pricePerKg should be(17)
  }

  it should "add all quantities together" in {
    val f = fixture
    val summaryOrder = SummaryOrder.from(aValidOrder.copy(quantityInKg = 23))
    val orders = List(summaryOrder, summaryOrder.copy(quantityInKg = 6), summaryOrder.copy(quantityInKg = 24))
    val summed = f.service.sumQuantityInKg(orders)
    summed.quantityInKg should be(53)
  }
}
