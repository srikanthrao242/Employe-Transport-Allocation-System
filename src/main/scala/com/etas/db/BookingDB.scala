package com.etas.db

import cats.Show
import com.etas.db.DBUtil.Dao
import com.etas.entities._
import doobie._
import doobie.implicits._

import scala.util.{Failure, Success, Try}

trait BookingDB {

  val employeeDB: EmployeeDB

  implicit val dao: Dao.Aux[BookingReq, Int] =
    Dao.derive[BookingReq, Int]("booking", "id")
  implicit val show: Show[BookingReq] = Show.fromToString
  val dn = Dao[BookingReq]

  import dn._

  def insertBooking(ps: BookingReq): Int = insert(ps).transact(MysqlExec.xa).unsafeRunSync()

  def updateStatus(id: Int, avl: String): Int =
    sql"update booking set status = '$avl'  where id = $id".update.run.transact(MysqlExec.xa).unsafeRunSync()

  def getRequestDetails(id: Int): Option[BookingRequestStatus] = {
    val query = Query[Int, BookReqStatus](
      s"""
                SELECT id, requestStatus, sourceLocation,dateTimeOfJourney,requestCreationDate,requestGenerator,comments
                FROM booking
                WHERE id = ?
              """).option(id)
    Try(query.transact(MysqlExec.xa).unsafeRunSync()) match {
      case Success(Some(value)) =>
        val emp = employeeDB.getEmployee(value.requestGenerator).get
        Some(BookingRequestStatus(value.id, value.requestStatus,
          value.comments, None, value.sourceLocation, value.dateTimeOfJourney,
          value.requestCreationDate, emp)
        )
      case Failure(e) => e.printStackTrace()
        throw new Exception(s"Booking details not found : $id")
      case _ => throw new Exception(s"Booking details not found : $id")
    }
  }

  def getBookingDetails(id: Int): Option[BookingReq] = {
    find(id).transact(MysqlExec.xa).unsafeRunSync()
  }
}

object BookingDB{
  def apply(): BookingDB = new BookingDB{
    override val employeeDB: EmployeeDB = EmployeeDB.apply()
  }
}
