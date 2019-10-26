package com.etas.db

import cats.Show
import com.etas.db.DBUtil.Dao
import com.etas.entities.Driver
import doobie.implicits._
import doobie._

import scala.util.{Failure, Success, Try}
object DriverDB {

  implicit val dao: Dao.Aux[Driver, Int] =
    Dao.derive[Driver, Int]("driver", "id")
  implicit val show: Show[Driver] = Show.fromToString
  val dn = Dao[Driver]
  import dn._

  def getVehAndSources: List[Driver] = findAll.transact(MysqlExec.xa).compile.toList.unsafeRunSync()

  def getDriverId(source: String) : Option[Int] = {
    Try{sql"select driver_id from driver where source = '$source'"
      .query[Int].unique.transact(MysqlExec.xa).unsafeRunSync()} match {
      case Success(value) => Some(value)
      case Failure(exception) => exception.printStackTrace()
        None
    }
  }
}

