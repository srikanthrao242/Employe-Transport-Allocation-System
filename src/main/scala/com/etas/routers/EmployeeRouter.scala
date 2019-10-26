package com.etas.routers

import java.io.{PrintWriter, StringWriter}

import akka.http.scaladsl.server.{ExceptionHandler, Route, RouteConcatenation}
import com.etas.AkkaCoreModule
import akka.http.scaladsl.server.Directives._
import com.etas.entities.Employee
import com.etas.entities.EntityAndSer._
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import DefaultJsonProtocol._
import com.etas.db.EmployeeDB

trait EmployeeRouter extends RouteConcatenation {
  this: AkkaCoreModule =>

  val exceptionHandler = ExceptionHandler {
    case exception: Exception =>
      val sw = new StringWriter
      exception.printStackTrace(new PrintWriter(sw))
      complete(s"uncaught exception: $sw")
  }

  val employeeRoutes: Route =
    path("employees") {
      get {
        complete(EmployeeDB.getEmployees)
      }
    } ~
      pathPrefix("employee") {
        {
          post {
            entity(as[Employee]) {
              v =>
                complete(EmployeeDB.insertEmployee(v).toString)
            }
          } ~
            put {
              entity(as[Employee]) {
                v => complete(EmployeeDB.updateEmployee(1,v).toString)
              }
            }
        } ~
          path(IntNumber) { id =>
            concat(
              get {
                complete {
                  EmployeeDB.getEmployee(id)
                }
              }
            )
          } ~
          path(IntNumber) { id =>
            concat(
              delete {
                complete {
                  EmployeeDB.deleteEmployee(id).toString
                }
              }
            )
          }
      }

}
