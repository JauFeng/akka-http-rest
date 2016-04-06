package com.turing.rest

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._

/**
 * Routes test.
 *
 * Testing Exception Handler & Rejection Handler
 */
class RoutesSpec extends BaseRoutesSpec {

  // This exception handler is what you want to test.
  private val exceptionHandler = ExceptionHandler {
    case _: ArithmeticException => complete((StatusCodes.BadRequest, "You've got your arithmetic wrong, fool!"))
  }

  // This rejection handler is what you want to test.
  private val rejectionHandler = RejectionHandler.newBuilder()
    .handleNotFound(complete((StatusCodes.NotFound, "Oh my god, what you are looking for is long gone.")))
    .handle { case ValidationRejection(msg, _) => complete((StatusCodes.InternalServerError, msg)) }
    .result()

  val handler = handleExceptions(exceptionHandler) & handleRejections(rejectionHandler)

  val routes =
    pathPrefix("handled") {
      handler {
        path("existing") {
          complete("This path exists.")
        } ~
          path("boom") {
            reject(new ValidationRejection("Something didn't work."))
          } ~
          path("divide" / IntNumber) { a =>
            complete(s"The result is ${10 / a}.")
          }
      }
    }

  " Exception Handling " should " handling exceptions " in {
    Get("/handled/divide/5") ~> routes ~> check {
      info(s"Response: $status, ${responseAs[String]}")
      responseAs[String] shouldEqual "The result is 2."
    }
  }
  it should " handling exceptions with internal server error " in {
    Get("/handled/divide/0") ~> routes ~> check {
      info(s"Response: $status, ${responseAs[String]}")
      status shouldEqual StatusCodes.BadRequest
      responseAs[String] shouldEqual "You've got your arithmetic wrong, fool!"
    }
  }

  " Rejection Handling " should " handling existing " in {
    Get("/handled/existing") ~> routes ~> check {
      info(s"Response: $status, ${responseAs[String]}")
      responseAs[String] shouldEqual "This path exists."
    }
  }
  it should " handling missing with default rejection handler " in {
    Get("/missing") ~> Route.seal(routes) ~> check {
      info(s"Response: $status, ${responseAs[String]}")
      status shouldEqual StatusCodes.NotFound
      responseAs[String] shouldEqual "The requested resource could not be found."
    }
  }
  it should " handling missing with custom rejection handler " in {
    Get("/handled/missing") ~> routes ~> check {
      info(s"Response: $status, ${responseAs[String]}")
      status shouldEqual StatusCodes.NotFound
      responseAs[String] shouldEqual "Oh my god, what you are looking for is long gone."
    }
  }
  it should " handling error when internal server is wrong " in {
    Get("/handled/boom") ~> routes ~> check {
      info(s"Response: $status, ${responseAs[String]}")
      status shouldEqual StatusCodes.InternalServerError
      responseAs[String] shouldEqual "Something didn't work."
    }
  }
}
