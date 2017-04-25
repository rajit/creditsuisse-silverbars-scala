package co.qeek.silverbars.liveordersboard

import co.qeek.silverbars.liveordersboard.domain.{Order, OrderType}

object LiveOrdersBoardFixture {
  val aValidOrder = Order(userId = 1234, quantityInKg = 10, pricePerKg = 303.0, orderType = OrderType.BUY)
}