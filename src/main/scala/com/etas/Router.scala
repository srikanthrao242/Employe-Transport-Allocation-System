package com.etas

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.{ExceptionHandler, RouteConcatenation}
import com.etas.routers.{BookRouter, CabRouter, EmployeeRouter}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server._
import Directives._

sealed trait SuperTrait

trait Router extends SuperTrait
  with RouteConcatenation
  with BookRouter
  with EmployeeRouter
  with CabRouter{
  this: AkkaCoreModule =>

  implicit val exceptionHandler = ExceptionHandler {
    case exception: Exception =>
        complete(HttpResponse(InternalServerError, entity = exception.getMessage))
  }

  val mainRoute : Route=
    handleExceptions(exceptionHandler){
      employeeRoutes ~ cabRoutes ~ bookRoutes
    }

}
