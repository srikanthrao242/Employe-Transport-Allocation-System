package com.etas

import akka.http.scaladsl.server.RouteConcatenation
import com.etas.routers.{CabRouter, EmployeeRouter}


sealed trait SuperTrait

trait Router extends SuperTrait
  with RouteConcatenation
  with EmployeeRouter
  with CabRouter {
  this: AkkaCoreModule =>
  val mainRoute = employeeRoutes ~ cabRoutes
}
