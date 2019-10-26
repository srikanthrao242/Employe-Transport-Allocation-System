package com.etas.routers

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.RouteConcatenation
import com.etas.AkkaCoreModule
import com.etas.db.CabDB
import com.etas.entities.Cabs
import com.etas.entities.EntityAndSer._
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import DefaultJsonProtocol._

trait CabRouter extends RouteConcatenation {
  this: AkkaCoreModule =>

  val cabRoutes = path("cabs") {
    get {
      complete(CabDB.getCabs)
    }
  } ~
    pathPrefix("cabs") {
      {
        post {
          entity(as[Cabs]) {
            v =>
              complete(CabDB.insertCab(v).toString)
          }
        } ~
          put {
            entity(as[Cabs]) {
              v => complete(CabDB.updateCab(1, v).toString)
            }
          }
      } ~
        path(IntNumber) { id =>
          concat(
            get {
              complete {
                CabDB.getCab(id)
              }
            }
          )
        } ~
        path(IntNumber) { id =>
          concat(
            delete {
              complete {
                CabDB.deleteCab(id).toString
              }
            }
          )
        } ~
        path(IntNumber / Segment) { (id, avl) => {
          concat(
            put {
              complete {
                CabDB.updateAvailability(id, avl).toString
              }
            }
          )
        }
        }
    }

}
