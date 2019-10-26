package com.etas.entities

import spray.json._
import DefaultJsonProtocol._

case class ResponseErrors(ERROR_CODE: String, description: Option[String]) extends Responses

object ResponseErrors {

  implicit val responseErrorsSer: RootJsonFormat[ResponseErrors] = jsonFormat2(ResponseErrors.apply)

  def getErrorRes(error_code: String): ResponseErrors = {
    error_code match {
      case "CAB_NOT_AVAILABLE" => ResponseErrors("CAB_NOT_AVAILABLE", None)
      case "SOURCE_INVALID" => ResponseErrors("SOURCE_INVALID", None)
      case "INVALID_TRIP_TIME" => ResponseErrors("INVALID_TRIP_TIME", None)
      case "REQUEST_NOT_POSSIBLE" => ResponseErrors("REQUEST_NOT_POSSIBLE", None)
      case _ => ResponseErrors("OTHERS", None)
    }
  }


}
