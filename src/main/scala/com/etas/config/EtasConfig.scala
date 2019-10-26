package com.etas.config

import pureconfig._
import pureconfig.generic.ProductHint
import pureconfig.generic.auto._
case class DB(url: String, driver: String, username: String, password: String, connectTimeout: String)
case class HttpConfig(host:String, port: Int)
case class ETAS(db: DB, http: HttpConfig)
case class EtasConf(etas: ETAS)

object EtasConfig {
  implicit def productHint[T]: ProductHint[T] = ProductHint(ConfigFieldMapping(CamelCase, CamelCase))
  val config = ConfigSource.default.load[EtasConf] match {
    case Right(c) => c.etas
    case Left(e) =>
      throw new RuntimeException("Config error: " + e)
  }
}
