package co.qeek.silverbars.liveordersboard.services

import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}

import co.qeek.silverbars.liveordersboard.domain.{Order, OrderType, SummaryOrder}

import scala.collection.JavaConverters._

class LiveOrdersBoardService {
  type OrderId = Long

  private val orders: ConcurrentMap[OrderId, Order] = new ConcurrentHashMap()
  private val nextId = new AtomicLong(1)

  def registerOrder(order: Order): Either[Throwable, OrderId] =
    if (order.quantityInKg < 0) {
      Left(OrderInvalidException)
    } else if (order.pricePerKg < 0) {
      Left(OrderInvalidException)
    } else {
      val orderId = nextId.getAndIncrement()
      orders.put(orderId, order)
      Right(orderId)
    }

  def cancelOrder(orderId: OrderId): Either[Throwable, Unit] =
    Option(orders.remove(orderId)) match {
      case Some(_) =>
        Right(())
      case None =>
        Left(OrderNotFoundException)
    }

  def summary(orderType: OrderType): List[SummaryOrder] = {
    val summarised = orders.values().asScala.toList
      .filter(_.orderType == orderType)
      .map(SummaryOrder.from)
      .groupBy(_.pricePerKg)
      .values
      .map(sumQuantityInKg)
      .toList

    orderType match {
      case OrderType.BUY => summarised.sortBy(_.pricePerKg)(Ordering.BigDecimal.reverse)
      case OrderType.SELL => summarised.sortBy(_.pricePerKg)
    }
  }

  // Assumes all prices are the same/takes first
  def sumQuantityInKg(orders: Seq[SummaryOrder]): SummaryOrder =
    orders.reduce((a, b) => a.copy(quantityInKg = a.quantityInKg + b.quantityInKg))
}

case object OrderInvalidException extends RuntimeException
case object OrderNotFoundException extends RuntimeException
