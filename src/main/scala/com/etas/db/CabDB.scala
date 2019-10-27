package com.etas.db

import cats.Show
import com.etas.db.DBUtil.Dao
import com.etas.entities.Cabs
import doobie._
import doobie.implicits._

import scala.util.{Failure, Success, Try}

trait CabDB {

  implicit val dao: Dao.Aux[Cabs, Int] =
    Dao.derive[Cabs, Int]("cab", "id")
  implicit val show: Show[Cabs] = Show.fromToString

  val dn = Dao[Cabs]

  import dn._

  def getCabs: List[Cabs] = findAll.transact(MysqlExec.xa).compile.toList.unsafeRunSync()

  def getCab(id: Int): Option[Cabs] = find(id).transact(MysqlExec.xa).unsafeRunSync()

  def deleteCab(id: Int): Int = delete(id).transact(MysqlExec.xa).unsafeRunSync()

  def insertCab(ps: Cabs): Int = insert(ps).transact(MysqlExec.xa).unsafeRunSync()

  def updateCab(id: Int, cab: Cabs): Int = update(id, cab).transact(MysqlExec.xa).unsafeRunSync()

  def updateAvailability(id: Int, avl: String): Int =
    sql"update cab set available = '$avl'  where id = $id".update.run.transact(MysqlExec.xa).unsafeRunSync()

  def updateVacancy(id: Int, vac: Int): Int =
    sql"update cab set vacancy = $vac  where id = $id".update.run.transact(MysqlExec.xa).unsafeRunSync()

  def isSourceValid(source: String): Boolean = {

    val query = Query[String, Cabs](
      s"""
                SELECT a.registrationNumber, a.driverId, a.cabStatus, a.comments, a.vacancy
                FROM yantranet.cab a, yantranet.driver b
                where a.driverId = b.driver_id and b.sourceLocation = ?
              """).option(source)
    Try(query.transact(MysqlExec.xa).unsafeRunSync()) match {
      case Success(Some(_)) => true
      case _ => throw new Exception("isSourceValid failed")
    }
  }

  def checkStatus(source: String): Option[(Int,Cabs)] = {
    val query = Query[String, (Int,Cabs)](
      s"""
                SELECT a.id, a.registrationNumber, a.driverId, a.cabStatus, a.comments, a.vacancy
                FROM yantranet.cab a, yantranet.driver b
                where a.driverId = b.driver_id and a.vacancy < 5 and b.sourceLocation = ?
              """).option(source)
    Try(query.transact(MysqlExec.xa).unsafeRunSync()) match {
      case Success(Some(value)) => Some(value)
      case Failure(e) => e.printStackTrace()
        throw new Exception("checkstatus failed ")
      case _ => None
    }
  }

  def getCabByDriverId(id: Int): Option[Cabs] = {

    val query = Query[Int, Cabs](
      s"""
                SELECT registrationNumber, driverId, cabStatus, comments, vacancy
                FROM cab
                where driverId = ?
              """).option(id)
    Try(query.transact(MysqlExec.xa).unsafeRunSync()) match {
      case Success(Some(value)) => Some(value)
      case _ => throw new Exception(s"driver Id is not available : $id")
    }
  }


}

object CabDB{
  def apply(): CabDB = new CabDB{

  }
}
