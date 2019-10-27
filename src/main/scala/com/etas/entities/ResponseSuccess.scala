package com.etas.entities

import java.sql.Timestamp

import spray.json._
import DefaultJsonProtocol._

trait Responses

case class SuccessRes(requestId: Int) extends Responses

case class BookReqStatus(id: Int,
                         requestStatus: String,
                         sourceLocation: String,
                         dateTimeOfJourney: Timestamp,
                         requestCreationDate: Timestamp,
                         requestGenerator: Int,
                         comments: Option[String])

case class BookingRequestStatus(id: Int,
                                requestStatus: String,
                                comments: Option[String],
                                bookingId: Option[String],
                                sourceLocation: String,
                                dateTimeOfJourney: Timestamp,
                                requestCreationDate: Timestamp,
                                requestGenerator: Employee)
  extends Responses

case class BookingStatus(bookingId: Int,
                         sourceLocation: String,
                         dateTimeOfJourney: Timestamp,
                         bookingStatus: RequestStatus.Value,
                         passengerDetails: Employee,
                         vehicleDetails: Cabs,
                         driverDetails: Employee)
  extends Responses

object ResponseSuccessSer {
  import RequestStatusSer._
  import EntityAndSer._
  implicit val bookingSer: RootJsonFormat[BookingRequestStatus] = jsonFormat8(
    BookingRequestStatus
  )
  implicit val bookingStatusSer: RootJsonFormat[BookingStatus] = jsonFormat7(
    BookingStatus
  )
  implicit val successResSer: RootJsonFormat[SuccessRes] = jsonFormat1(
    SuccessRes
  )

  implicit def responsesFormat: RootJsonFormat[Responses] =
    new RootJsonFormat[Responses] {
      def write(a: Responses) = a match {
        case p: SuccessRes           => p.toJson
        case p: BookingRequestStatus => p.toJson
        case p: BookingStatus        => p.toJson
        case p: ResponseErrors       => p.toJson
      }
      def read(value: JsValue) = ???
    }

}
