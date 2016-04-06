package com.turing.rest.user

import spray.json.{RootJsonFormat, DefaultJsonProtocol}

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

/**
 * User Json for unmarshalling.
 */
object UserJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val userProtocolFormat: RootJsonFormat[User] = jsonFormat9(User)
  implicit val usersProtocolFormat = jsonFormat2(UserSeq)
}

final case class UserSeq(count: Int = 0, users: Seq[User] = Nil)