package com.etas.entities

import java.sql.Timestamp

import spray.json.{DeserializationException, JsString, JsValue, RootJsonFormat}

object RequestStatus extends Enumeration{
  type RequestStatus = Value
  val GENERATED, PROCESSED, FAILED, CLOSED, CANCELLED = Value
}

object FinalStatus extends Enumeration{
  type FinalStatus =Value
  val CONFIRMED, CANCELLED = Value
}

object RequestStatusSer{
  implicit def enumFormat[T <: Enumeration](implicit enu: T): RootJsonFormat[T#Value] =
    new RootJsonFormat[T#Value] {
      def write(obj: T#Value): JsValue = JsString(obj.toString)
      def read(json: JsValue): T#Value = {
        json match {
          case JsString(txt) => enu.withName(txt)
          case somethingElse => throw DeserializationException(s"Expected a value from enum $enu instead of $somethingElse")
        }
      }
    }
  implicit object DateJsonFormat extends RootJsonFormat[Timestamp] {
    override def write(obj: Timestamp) = JsString(obj.toString)
    override def read(json: JsValue) : Timestamp = json match {
      case JsString(s) => Timestamp.valueOf(s)
      case _ => throw new DeserializationException("Error info you want here ...")
    }
  }
  implicit val statusFormat: RootJsonFormat[RequestStatus.Value] = enumFormat(RequestStatus)
  implicit val finalStatusFormat: RootJsonFormat[FinalStatus.Value] = enumFormat(FinalStatus)
}

