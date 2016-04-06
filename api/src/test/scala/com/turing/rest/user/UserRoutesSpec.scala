package com.turing.rest.user

import com.google.inject.Guice
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.coding.Gzip
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directive1, Route}
import com.turing.rest.{BaseRoutesSpec, UnitSpec}

import scala.concurrent.Future

/**
 * User Routes Unit Test.
 */
class UserRoutesSpec extends UnitSpec with BaseRoutesSpec {
  spec =>

  //  val injector = Guice.createInjector(new UserServiceDependencyModule, new UserDALDependencyModule)
  //  val _userService = injector.getInstance[UserService](classOf[UserService])
  val mobile = 15001109204L

  val userMock = User(mobile = Some(mobile), name = Some("sean"), password = Some("pwd"),
    email = Some("freecloud@gmail.com"), gender = Some("female"), age = Some(20))

  lazy val _userService = mock[UserServiceComponent]

  val userRoutes = new UserRoutes() with TestRoutesSupport {
    override def userService = _userService
  }

  val routes = Route.seal(userRoutes.userRoutes)

  import UserJsonProtocol._

  " POST /users " should " create user " in {
    (_userService.create _) expects userMock returning Future(Right(1)) once ()
    Post("/users", userMock) ~> routes ~> check {
      status shouldEqual StatusCodes.OK
    }
  }

  s" GET /user/$mobile " should s" get user whose mobile number is $mobile " in {
    (_userService.findUserByMobile _) expects mobile returning Future(Some(userMock))
    Get(s"/user/$mobile") ~> routes ~> check {
      status shouldEqual StatusCodes.OK
    }
  }

  " GET /users " should " get users with default limit is 5 " in {
    (_userService.findUsers _) expects ("", 10) returning Future(UserSeq())
    Get("/users?offset&limit=10") ~> routes ~> check {
      status shouldEqual StatusCodes.OK
    }
  }

  " PUT /user " should " modify user " in {
    (_userService.findAndUpdate _) expects (userMock) returning Future(Some(User()))

    Put("/user", userMock) ~> routes ~> check {
      status shouldEqual StatusCodes.OK
    }
  }

  s" DELETE /user/$mobile " should s" delete user whose mobile number is $mobile " in {
    (_userService.findAndRemove _) expects mobile returning Future(Some(User()))
    Delete(s"/user/$mobile") ~> routes ~> check {
      status shouldEqual StatusCodes.OK
    }
  }
}
