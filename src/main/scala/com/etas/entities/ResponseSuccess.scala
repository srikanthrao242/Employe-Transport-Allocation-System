package com.etas.entities


import java.sql.Timestamp

import spray.json._
import DefaultJsonProtocol._

trait Responses

case class SuccessRes(requestId: Int) extends Responses

case class BookingRequestStatus(requestId: Int,
                          requestStatus: RequestStatus.Value,
                          comments: String,
                          bookingId: Option[String],
                          sourceLocation: String,
                          dateTimeOfJourney: Timestamp,
                          requestCreationDate: Timestamp,
                          requestGenerator: Employee) extends Responses

case class BookingStatus(bookingId: Int,
                         sourceLocation:String,
                         dateTimeOfJourney:Timestamp,
                         bookingStatus: RequestStatus.Value,
                         passengerDetails: Employee,
                         vehicleDetails: Cabs,
                         driverDetails: Employee) extends Responses

class ResponseSuccess {
  import RequestStatusSer._
  import EntityAndSer._
  implicit val bookingSer: RootJsonFormat[BookingRequestStatus] = jsonFormat8(BookingRequestStatus)
  implicit val bookingStatusSer: RootJsonFormat[BookingStatus] = jsonFormat7(BookingStatus)
}
