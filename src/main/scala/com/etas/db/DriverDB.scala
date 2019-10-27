package com.etas.db

import cats.Show
import com.etas.db.DBUtil.Dao
import com.etas.entities.Driver
import doobie.implicits._
import doobie._

import scala.util.{ Success, Try}
trait DriverDB {
  implicit val dao: Dao.Aux[Driver, Int] =
    Dao.derive[Driver, Int]("driver", "id")
  implicit val show: Show[Driver] = Show.fromToString
  val dn = Dao[Driver]
  import dn._

  def getVehAndSources: List[Driver] = findAll.transact(MysqlExec.xa).compile.toList.unsafeRunSync()

  def getDriverId(source: String) : Option[Int] = {
    val query = Query[String, Int](
      s"""
                SELECT driver_id
                FROM driver
                where sourceLocation = ?
              """).option(source)
    Try{query.transact(MysqlExec.xa).unsafeRunSync()} match {
      case Success(Some(value)) => Some(value)
      case _ => throw new Exception("getDriverId failed")
    }
  }
}

object DriverDB{
  def apply(): DriverDB = new DriverDB{}
}

