package co.qeek.silverbars.liveordersboard.domain

case class Order(userId: Long, quantityInKg: Double, pricePerKg: BigDecimal, orderType: OrderType)

sealed trait OrderType
object OrderType {
  def fromString(string: String): Option[OrderType] = string match {
    case "buy" => Some(BUY)
    case "sell" => Some(SELL)
    case _ => None
  }
  case object BUY extends OrderType
  case object SELL extends OrderType
}

object SummaryOrder {
  def from(order: Order) = SummaryOrder(order.quantityInKg, order.pricePerKg)
}

case class SummaryOrder(quantityInKg: Double, pricePerKg: BigDecimal)
