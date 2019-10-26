package com.etas.db

import cats.Show
import com.etas.db.DBUtil.Dao
import com.etas.entities.Cabs
import doobie.implicits._

import scala.util.{Failure, Success, Try}
object CabDB {

  implicit val dao: Dao.Aux[Cabs, Int] =
    Dao.derive[Cabs, Int]("cab", "id")
  implicit val show: Show[Cabs] = Show.fromToString

  val dn = Dao[Cabs]

  import dn._

  def getCabs: List[Cabs] =  findAll.transact(MysqlExec.xa).compile.toList.unsafeRunSync()

  def getCab(id: Int): Option[Cabs] = find(id).transact(MysqlExec.xa).unsafeRunSync()

  def deleteCab(id: Int): Int = delete(id).transact(MysqlExec.xa).unsafeRunSync()

  def insertCab(ps: Cabs): Int = insert(ps).transact(MysqlExec.xa).unsafeRunSync()

  def updateCab(id: Int, cab: Cabs): Int = update(id, cab).transact(MysqlExec.xa).unsafeRunSync()

  def updateAvailability(id: Int, avl: String):Int=
    sql"update cab set available = '$avl'  where id = $id".update.run.transact(MysqlExec.xa).unsafeRunSync()

  def updateVacancy(id: Int, vac: String):Int=
    sql"update cab set vacancy = $vac  where id = $id".update.run.transact(MysqlExec.xa).unsafeRunSync()

  def isSourceValid(source: String): Boolean={
    Try(sql"select a.* from yantranet.cab a, yantranet.driver b where a.id = b.driver_id and b.sourceLocation = '$source'"
      .query[Cabs].unique.transact(MysqlExec.xa).unsafeRunSync()) match {
      case Success(value) => true
      case Failure(exception) => exception.printStackTrace()
        false
    }
  }
  def checkStatus(source: String): Option[Cabs] ={
    Try(sql"select a.* from yantranet.cab a, yantranet.driver b where a.id = b.driver_id and a.vacancy < 5 and b.sourceLocation = '$source'"
      .query[Cabs].unique.transact(MysqlExec.xa).unsafeRunSync()) match {
      case Success(value) => Some(value)
      case Failure(exception) => exception.printStackTrace()
        None
    }
  }

  def getCabByDriverId(id: Int):Option[Cabs]={
    Try(sql"select * from yantranet.cab where driverId = $id"
      .query[Cabs].unique.transact(MysqlExec.xa).unsafeRunSync()) match {
      case Success(value) => Some(value)
      case Failure(exception) => exception.printStackTrace()
        None
    }
  }


}
