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

case class UpdateEmployee(full_name: Option[String],
                          designation: Option[String],
                          email: Option[String],
                          phone: Option[String],
                          address: Option[String])

case class UpdateEmployeeReq(id:Int, details: UpdateEmployee)

case class Cabs(registrationNumber: Int,
                driverId: Int,
                cabStatus: String,
                comments: String,
                vacancy: Int)

case class UpdateCab(registrationNumber: Option[Int],
                     driverId: Option[Int],
                     cabStatus: Option[String],
                     comments: Option[String])

case class UpdateCabReq(id: Int, details: UpdateCab)

case class CabRequest(sourceLocation: String,
                      dateTimeOfJourney: Timestamp,
                      employeeId: Int)

case class CheckStatus(bookingId: String,
                       sourceLocation: String,
                       dateTimeOfJourney: Timestamp,
                       bookingStatus: RequestStatus.Value,
                       passengerDetails: Employee,
                       driverDetails: Employee)

case class BookingReq(requestStatus: String,
                      sourceLocation: String,
                      dateTimeOfJourney: Timestamp,
                      requestGenerator: Int)

case class Driver(id: Int,
                  driver_id: Int,
                  sourceLocation: String)

object EntityAndSer {

  import RequestStatusSer._

  implicit val employeeSer: RootJsonFormat[Employee] = jsonFormat6(Employee)
  implicit val uEmployeeSer: RootJsonFormat[UpdateEmployee] = jsonFormat5(UpdateEmployee)
  implicit val uEmployeeReqSer: RootJsonFormat[UpdateEmployeeReq] = jsonFormat2(UpdateEmployeeReq)
  implicit val cabsSer: RootJsonFormat[Cabs] = jsonFormat5(Cabs)
  implicit val uCabsSer: RootJsonFormat[UpdateCab] = jsonFormat4(UpdateCab)
  implicit val uCabsReqSer: RootJsonFormat[UpdateCabReq] = jsonFormat2(UpdateCabReq)
  implicit val cabRequestsSer: RootJsonFormat[CabRequest] = jsonFormat3(CabRequest)
  implicit val checkStatusSer: RootJsonFormat[CheckStatus] = jsonFormat6(CheckStatus)
  implicit val vehicleSer: RootJsonFormat[Driver] = jsonFormat3(Driver)
}


