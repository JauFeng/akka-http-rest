package com.turing.rest.user

import akka.http.scaladsl.model._
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.directives.ParameterDirectives.ParamMagnet
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.coding.Gzip

import com.turing.rest.BaseRoutes

import scala.concurrent.Future

/**
 * User Routes.
 */
trait UserRoutes extends BaseRoutes {

  import UserJsonProtocol._

  /**
   * User service implemented in Bean.
   *
   * @return
   */
  def userService: UserServiceComponent

  /**
   * User routes.
   */
  val userRoutes: Route =
    pathPrefix("users") {
      (pathEnd & get & parameters('offset.as[String] ? "", 'limit.as[Int] ? 10)) { (offset, limit) =>
        encodeResponseWith(Gzip) {
          complete {
            userService.findUsers(offset, limit).map[ToResponseMarshallable] {
              userSeq =>
                StatusCodes.OK -> userSeq
            }
          }
        }
      } ~ (pathEnd & post & decodeRequest & entity(as[User])) { user =>
        complete {
          userService.create(user) map {
            case Left(e) => HttpResponse(StatusCodes.InternalServerError, entity =
              HttpEntity(
                ContentTypes.`application/json`,
                s"""
                   |{
                   | "statusCode": "${StatusCodes.InternalServerError.intValue}",
                   | "errMsg": "${StatusCodes.InternalServerError.defaultMessage} : ${e.getMessage}"
                   | }""".stripMargin
              ))
            case Right(n) => HttpResponse(StatusCodes.OK, entity =
              HttpEntity(
                ContentTypes.`application/json`,
                s"""
                   |{
                   | "statusCode" : "${StatusCodes.OK.intValue}",
                   | "modifiedNum" : "$n"
                   | }
                   """.stripMargin
              ))
          }
        }
      }
    } ~ pathPrefix("user") {
      (path(LongNumber) & pathEnd) { mobile =>
        (get & encodeResponseWith(Gzip)) {
          complete {
            userService.findUserByMobile(mobile).map[ToResponseMarshallable] {
              case Some(user) => StatusCodes.OK -> user
              case None => StatusCodes.NotFound -> User()
            }
          }
        } ~ delete {
          complete {
            userService.findAndRemove(mobile).map[ToResponseMarshallable] {
              case Some(user) => StatusCodes.OK -> user
              case None => StatusCodes.NotFound -> User()
            }
          }
        }
      } ~ (pathEnd & put & decodeRequest & entity(as[User])) { user =>
        complete {
          userService.findAndUpdate(user).map[ToResponseMarshallable] {
            case Some(resultUser) => StatusCodes.OK -> resultUser
            case None => StatusCodes.NotFound -> User()
          }
        }
      }
    }
}
