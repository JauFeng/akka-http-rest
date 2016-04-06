package com.turing.rest.authentication

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{RootJsonFormat, DefaultJsonProtocol}

object AuthenticationJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val registerFormProtocolFormat: RootJsonFormat[RegistrationForm] = jsonFormat7(RegistrationForm)
  implicit val loginFormProtocolFormat = jsonFormat2(LoginForm)
}