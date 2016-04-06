package com.turing.rest.authentication

/**
 * Created by sean on 16-3-5.
 */

trait AuthenticationResult

object AuthenticationResult {

  case class InvalidData(message: String) extends AuthenticationResult

  case class MobileNotExists(message: String) extends AuthenticationResult

  case class Success(token: String) extends AuthenticationResult

}
