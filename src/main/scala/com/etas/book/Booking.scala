package com.etas.book

import java.sql.Timestamp
import java.time._
import java.util.{Calendar, Date}

import com.etas.db.{BookingDB, CabDB, DriverDB, EmployeeDB}
import com.etas.entities._

trait Booking {

  val cabDb: CabDB
  val employeeDB:EmployeeDB
  val driverDB:DriverDB
  val bookingDB:BookingDB

  def isWeekDay(journeyTime: Timestamp): Boolean = {
    val cal = Calendar.getInstance()
    cal.setTime(journeyTime)
    cal.get(java.util.Calendar.DAY_OF_WEEK) < 6
  }

  def isAfter12Hours(journeyTime: Timestamp): Boolean = {
    val current = new Timestamp(new Date().getTime)
    val cal = Calendar.getInstance()
    cal.setTimeInMillis(current.getTime)
    cal.add(Calendar.HOUR, 12)
    val startDate = new Timestamp(cal.getTime.getTime)
    val start = ZonedDateTime.ofInstant(startDate.toInstant, ZoneId.systemDefault());
    val  end = ZonedDateTime.ofInstant(journeyTime.toInstant, ZoneId.systemDefault());
    val diff = Duration.between(start, end)
    diff.toHours >= 12 && isWeekDay(journeyTime)
  }

  def isSourceIsValid(source: String): Boolean = {
    cabDb.isSourceValid(source)
  }

  def book(req: CabRequest): Responses = {
    if (isAfter12Hours(req.dateTimeOfJourney)) {
      if (isSourceIsValid(req.sourceLocation)) {
        cabDb.checkStatus(req.sourceLocation) match {
          case Some((id,cab)) =>
            val booking = BookingReq(RequestStatus.CONFIRMED.toString,
              req.sourceLocation,
              req.dateTimeOfJourney,
              req.employeeId)
            cabDb.updateVacancy(id, cab.vacancy + 1)
            SuccessRes(bookingDB.insertBooking(booking))
          case None =>
            ResponseErrors.getErrorRes("CAB_NOT_AVAILABLE")
        }
      } else {
        ResponseErrors.getErrorRes("SOURCE_INVALID")
      }
    } else {
      ResponseErrors.getErrorRes("INVALID_TRIP_TIME")
    }
  }

  def getRequestDetails(id: Int) =
    bookingDB.getRequestDetails(id)

  def checkStatus(reqId: Int): Responses = {
    bookingDB.getBookingDetails(reqId) match {
      case Some(booking) =>
        val emp = employeeDB.getEmployee(booking.requestGenerator).get
        val driverId = driverDB.getDriverId(booking.sourceLocation).get
        val cab = cabDb.getCabByDriverId(driverId).get
        val driver = employeeDB.getEmployee(driverId).get
        BookingStatus(reqId,
          booking.sourceLocation,
          booking.dateTimeOfJourney,
          RequestStatus.withName(booking.requestStatus),
          emp, cab, driver
        )
      case None =>
        ResponseErrors.getErrorRes("CAB_NOT_AVAILABLE")
    }
  }

  def cancelBooking(reqId: Int): Responses ={
    bookingDB.getBookingDetails(reqId) match {
      case Some(booking) =>
        cabDb.getCabBySource(booking.sourceLocation) match {
          case Some((id, cab)) =>
            bookingDB.deleteBooking(reqId)
            SuccessRes(cabDb.updateVacancy(id, cab.vacancy - 1))
          case None =>
            ResponseErrors.getErrorRes("CAB_NOT_AVAILABLE")
        }
      case None =>
        ResponseErrors.getErrorRes("CAB_NOT_AVAILABLE")
    }
  }

}


