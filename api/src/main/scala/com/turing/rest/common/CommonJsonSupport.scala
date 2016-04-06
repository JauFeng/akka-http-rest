package com.turing.rest.common

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{RootJsonFormat, DefaultJsonProtocol}

/**
 * Common Json for unmarshalling.
 *
 * Status
 * Error
 *
 */
object CommonJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val statusWrapperFormat = jsonFormat2(StatusWrapper)
  implicit val errorWrapperFormat = jsonFormat3(ErrorWrapper)
}
