package com.etas.routers

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.RouteConcatenation
import com.etas.AkkaCoreModule
import com.etas.db.CabDB
import com.etas.entities.{Cabs, UpdateCab, UpdateCabReq}
import com.etas.entities.EntityAndSer._
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import DefaultJsonProtocol._

trait CabRouter extends RouteConcatenation {
  this: AkkaCoreModule =>

  val cabDB : CabDB = CabDB.apply()

  val cabRoutes = path("cabs") {
    get {
      complete(cabDB.getCabs)
    }
  } ~
    pathPrefix("cabs") {
      {
        post {
          entity(as[Cabs]) {
            v =>
              complete(cabDB.insertCab(v).toString)
          }
        } ~
          put {
            entity(as[UpdateCabReq]) {
              v => complete(cabDB.updateCab( v.id,v.details).toString)
            }
          }
      } ~
        path(IntNumber) { id =>
          concat(
            get {
              complete {
                cabDB.getCab(id)
              }
            }
          )
        } ~
        path(IntNumber) { id =>
          concat(
            delete {
              complete {
                cabDB.deleteCab(id).toString
              }
            }
          )
        } ~
        path(IntNumber / Segment) { (id, avl) => {
          concat(
            put {
              complete {
                cabDB.updateAvailability(id, avl).toString
              }
            }
          )
        }
        }
    }

}
