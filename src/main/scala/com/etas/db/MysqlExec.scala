package com.etas.db

import cats.effect.{ Async, IO }
import doobie._, doobie.implicits._
import java.sql.ResultSet
import scala.Predef._
import doobie.util.ExecutionContexts
import com.etas.config.EtasConfig

object MysqlExec {
  val etasConfig = EtasConfig.config
  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)
  val xa = Transactor.fromDriverManager[IO](
    etasConfig.db.driver,
    etasConfig.db.url,
    etasConfig.db.username, etasConfig.db.password
  )
}
