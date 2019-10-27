package com.etas.routers

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.RouteConcatenation
import com.etas.AkkaCoreModule
import com.etas.book.Booking
import com.etas.entities._
import ResponseSuccessSer._
import EntityAndSer._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import com.etas.db.{BookingDB, CabDB, DriverDB, EmployeeDB}
import spray.json._

trait BookRouter extends RouteConcatenation {
  this: AkkaCoreModule =>

  val booking : Booking = new Booking {
    override val cabDb: CabDB = CabDB.apply()
    override val employeeDB:EmployeeDB = EmployeeDB.apply()
    override val driverDB:DriverDB = DriverDB.apply()
    override  val bookingDB:BookingDB = BookingDB.apply()
  }

  val bookRoutes = path("request") {
    post {
      entity(as[CabRequest]) {
        v =>
          complete(booking.book(v).toJson.toString)
      }
    }
  }~ pathPrefix("request") {
    path(IntNumber) { id =>
      concat(
        get {
          complete {
            booking.getRequestDetails(id).get.toJson.toString
          }
        }
      )
    }
  } ~ pathPrefix("booking") {
    path(IntNumber) { id =>
      concat(
        get {
          complete {
            booking.checkStatus(id).toJson.toString
          }
        }
      )
    }
  }

}
