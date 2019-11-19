package com.etas.routers


import akka.http.scaladsl.server.{Route, RouteConcatenation}
import com.etas.AkkaCoreModule
import akka.http.scaladsl.server.Directives._
import com.etas.entities.{Employee, UpdateEmployee, UpdateEmployeeReq}
import com.etas.entities.EntityAndSer._
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import DefaultJsonProtocol._
import com.etas.db.EmployeeDB

trait EmployeeRouter extends RouteConcatenation {
  this: AkkaCoreModule =>

  val employeeDB: EmployeeDB = EmployeeDB.apply()

  val employeeRoutes: Route =
    path("employees") {
      get {
        complete(employeeDB.getEmployees)
      }
    } ~
      pathPrefix("employee") {
        {
          post {
            entity(as[Employee]) {
              v =>
                complete(employeeDB.insertEmployee(v).toString)
            }
          } ~
            put {
              entity(as[UpdateEmployeeReq]) {
                v => complete(employeeDB.updateEmployee(v.id,v.details).toString)
              }
            }
        } ~
          path(IntNumber) { id =>
            concat(
              get {
                complete {
                  employeeDB.getEmployee(id)
                }
              }
            )
          } ~
          path(IntNumber) { id =>
            concat(
              delete {
                complete {
                  employeeDB.deleteEmployee(id).toString
                }
              }
            )
          }
      }

}
