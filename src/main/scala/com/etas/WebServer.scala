package com.etas

import akka.event.jul.Logger
import akka.http.scaladsl.Http
import com.etas.config.EtasConfig


trait WebServer {
  this: AkkaCoreModule
    with Router =>

  val log = Logger.apply(this.getClass.getName)

  private val host = EtasConfig.config.http.host
  private val port = EtasConfig.config.http.port

  private val binding = Http().bindAndHandle(mainRoute, host, port)
  log.info(s"server listening on port $port")

}
