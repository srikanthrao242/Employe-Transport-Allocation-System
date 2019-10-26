package com.etas.db

import cats.Show
import com.etas.db.DBUtil.Dao
import com.etas.entities.Booking
import doobie.implicits._

object BookingDB {

  implicit val dao: Dao.Aux[Booking, Int] =
    Dao.derive[Booking, Int]("booking", "id")
  implicit val show: Show[Booking] = Show.fromToString
  val dn = Dao[Booking]
  import dn._

  def insertBooking(ps: Booking): Int = insert(ps).transact(MysqlExec.xa).unsafeRunSync()

  def updateStatus(id: Int, avl: String):Int=
    sql"update booking set status = '$avl'  where id = $id".update.run.transact(MysqlExec.xa).unsafeRunSync()

  def getBookingDetails(id: Int): Option[Booking] ={
    find(id).transact(MysqlExec.xa).unsafeRunSync()
  }
}
