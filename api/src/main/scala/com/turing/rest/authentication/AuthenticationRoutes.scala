package com.turing.rest.authentication

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import com.turing.rest.BaseRoutes

/**
 * Authentication Routes.
 */
trait AuthenticationRoutes extends BaseRoutes {

  import AuthenticationJsonProtocol._

  /**
   * Authentication service.
   */
  def authenticationService: AuthenticationService

  /**
   * Authentication routes.
   */
  val authenticationRoutes: Route =
    pathPrefix("auth") {
      (path("register") & pathEnd & post & decodeRequest & entity(as[RegistrationForm])) { registrationForm =>
        complete {
          StatusCodes.OK -> registrationForm
        }
      } ~ (path("login") & pathEnd & post & decodeRequest & entity(as[LoginForm])) { loginForm =>
        complete {
          authenticationService.login(loginForm).map[ToResponseMarshallable] {
            case AuthenticationResult.Success(token) =>
              StatusCodes.OK -> token
            case AuthenticationResult.MobileNotExists(message) =>
              StatusCodes.BadRequest -> message
            case AuthenticationResult.InvalidData(message) =>
              StatusCodes.BadRequest -> message
          }
        }
      }
    }
}
