package co.qeek.silverbars.liveordersboard.server

import co.qeek.silverbars.liveordersboard.http.LiveOrdersBoardHttpService
import org.http4s.server.blaze._
import org.http4s.server.{Server, ServerApp}

import scalaz.concurrent.Task

object LiveOrdersBoardServer extends ServerApp {
  override def server(args: List[String]): Task[Server] =
    BlazeBuilder
      .bindHttp(8080, "localhost")
      .mountService(LiveOrdersBoardHttpService.service, "/api")
      .start
}
