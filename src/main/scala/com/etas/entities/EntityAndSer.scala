package com.etas.entities

import spray.json._
import DefaultJsonProtocol._
import java.sql.Timestamp

case class Employee(full_name: String,
                    designation: String,
                    joiningDate: Timestamp,
                    email: String,
                    phone: String,
                    address: String)

case class Cabs(registrationNumber: Int,
                driverId: Int,
                cabStatus: String,
                comments: String,
                vacancy: Int)

case class CabRequest(sourceLocation: String,
                      dateTimeOfJourney: Timestamp,
                      employeeId: Int)

case class CheckStatus(bookingId: String,
                       sourceLocation: String,
                       dateTimeOfJourney: Timestamp,
                       bookingStatus: RequestStatus.Value,
                       passengerDetails: Employee,
                       driverDetails: Employee)
case class Booking(status: RequestStatus.Value,
                   sourceLocation: String,
                   dateTimeOfJourney: Timestamp,
                   emp_id: Int)

case class Driver(id: Int,
                  driver_id: Int,
                  sourceLocation: String)

object EntityAndSer {

  import RequestStatusSer._

  implicit val employeeSer: RootJsonFormat[Employee] = jsonFormat6(Employee)
  implicit val cabsSer: RootJsonFormat[Cabs] = jsonFormat5(Cabs)
  implicit val cabRequestsSer: RootJsonFormat[CabRequest] = jsonFormat3(CabRequest)
  implicit val checkStatusSer: RootJsonFormat[CheckStatus] = jsonFormat6(CheckStatus)
  implicit val vehicleSer: RootJsonFormat[Driver] = jsonFormat3(Driver)
}


