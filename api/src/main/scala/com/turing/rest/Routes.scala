package com.turing.rest

import akka.util.ByteString
import com.turing.rest.authentication.AuthenticationRoutes
import com.turing.rest.common.ErrorWrapper
import com.turing.rest.user.UserRoutes

import com.typesafe.scalalogging.StrictLogging

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.{RejectionHandler, ExceptionHandler}

/**
 * HTTP Routes.
 */
trait Routes extends StrictLogging
    with AuthenticationRoutes with UserRoutes {

  /**
   * Log duration.
   */
  private val logDuration = extractRequestContext.flatMap { ctx =>
    val start = System.currentTimeMillis()
    mapResponse { response =>
      val d = System.currentTimeMillis() - start
      logger.info(s"[${response.status}] ${ctx.request.method.name} ${ctx.request.uri}, took: ${d}ms")
      response
    }
  }

  /**
   * Exception Handling.
   */
  implicit def exceptionHandler = ExceptionHandler {
    case e: Exception =>
      extractUri { uri =>
        logger.error(
          s"""
             |Request to $uri could not be handled normally.
             |Exception during client request processing: ${e.getMessage}.
             |""".stripMargin
        )

        val errJson = ByteString(
          s"""
             |{
             | "statusCode": "${StatusCodes.InternalServerError.intValue}",
             | "errMsg": "${StatusCodes.InternalServerError.defaultMessage} : ${e.toString}"
             | }
          """.stripMargin
        )

        complete(HttpResponse(
          StatusCodes.InternalServerError,
          entity = HttpEntity(ContentTypes.`application/json`, errJson)
        ))
      }
  }

  /**
   * Rejection Handler.
   */
  implicit def rejectionHandler = RejectionHandler.default

  /**
   * Routes entry : `/api/v1`
   */
  val routes =
    logDuration {
      pathPrefix("api") {
        pathPrefix("v1") {
          authenticationRoutes ~ userRoutes
        }
      }
    }
}
