package com.etas.book

import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.{Calendar, Date}

import com.etas.db.{BookingDB, CabDB, DriverDB, EmployeeDB}
import com.etas.entities.{Booking, BookingStatus, CabRequest, RequestStatus, ResponseErrors, Responses, SuccessRes}

trait Booking {

  def isWeekDay(journeyTime: Timestamp):Boolean={
    val cal = Calendar.getInstance()
    cal.setTime(journeyTime)
    cal.get(java.util.Calendar.DAY_OF_WEEK) < 6
  }

  def isAfter12Hours(journeyTime: Timestamp): Boolean = {
    val current = new Timestamp(new Date().getTime)
    val cal = Calendar.getInstance()
    cal.setTimeInMillis(current.getTime)
    cal.add(Calendar.HOUR, 12)
    new Timestamp(cal.getTime.getTime) == journeyTime && isWeekDay(journeyTime)
  }
  def isSourceIsValid(source: String ): Boolean ={
    CabDB.isSourceValid(source)
  }
  def book(req: CabRequest): Responses = {
    if (isAfter12Hours(req.dateTimeOfJourney)) {
      if(isSourceIsValid(req.sourceLocation)) {
        CabDB.checkStatus(req.sourceLocation) match {
          case Some(_) =>
            val booking = Booking(RequestStatus.GENERATED,
              req.sourceLocation,
              req.dateTimeOfJourney,
              req.employeeId)
            SuccessRes(BookingDB.insertBooking(booking))
          case None =>
            ResponseErrors.getErrorRes("CAB_NOT_AVAILABLE")
        }
      }else{
        ResponseErrors.getErrorRes("SOURCE_INVALID")
      }
    }else{
      ResponseErrors.getErrorRes("INVALID_TRIP_TIME")
    }
  }

  def checkStatus(reqId: Int): Responses ={
    BookingDB.getBookingDetails(reqId) match {
      case Some(booking) =>
        val emp = EmployeeDB.getEmployee(booking.emp_id).get
        val driverId = DriverDB.getDriverId(booking.sourceLocation).get
        val cab = CabDB.getCabByDriverId(driverId).get
        val driver = EmployeeDB.getEmployee(driverId).get
        BookingStatus(reqId,
          booking.sourceLocation,
          booking.dateTimeOfJourney,
          booking.status,
          emp, cab, driver
        )
      case None =>
        ResponseErrors.getErrorRes("CAB_NOT_AVAILABLE")
    }
  }



}
