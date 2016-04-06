package com.turing.rest.user

import com.turing.rest.UnitSpec
import com.google.inject.Guice

import org.scalatest.concurrent.ScalaFutures._

import scala.util.{Success, Try}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * User Service Unit Test.
 */
class UserServiceSpec extends UnitSpec {
  val mockUserDAL = mock[UserDALComponent]

  lazy val userService = new UserService(mockUserDAL)

  " create user " should " be return Right(1) " in {
    val userSuccess = User(name = Some("Success"))
    (mockUserDAL.create _) expects userSuccess returning Future(Right(1)) once ()
    whenReady(userService.create(userSuccess)) { either =>
      either shouldBe Right(1)
    }
  }
  it should " be return Left(exception) when failure " in {
    val userFailure = User(name = Some("Failure"))
    val exception = new Exception("Something is wrong.")

    (mockUserDAL.create _) expects userFailure returning Future(Left(exception)) once ()
    whenReady(userService.create(userFailure)) { either =>
      either shouldEqual Left(exception)
    }
  }

  " find user by mobile " should " be return user which the mobile is " in {
    val mobile = 15001109204L
    (mockUserDAL.findUserByMobile _) expects mobile returning Future(Some(User(mobile = Some(mobile)))) once ()
    whenReady(userService.findUserByMobile(mobile)) { user =>
      user shouldBe Some(User(mobile = Some(mobile)))
    }
  }

  " find users " should " be return userSeq " in {
    val usersMock: List[User] = (1 to 5).toList map (i => User(name = Some(i.toString)))
    (mockUserDAL.findUsers _) expects ("", 10) returning Try(Future(usersMock)) once ()
    whenReady(userService.findUsers("", limit = 10)) { userSeq =>
      userSeq shouldEqual UserSeq(count = usersMock.size, users = usersMock)
    }
  }

  " find and update by mobile " should " be return user which the mobile is " in {
    val userBefore = User(name = Some("before"))
    val userAfter = User(name = Some("after"))
    (mockUserDAL.findAndUpdate _) expects (userBefore) returning Future(Some(userAfter)) once ()
    whenReady(userService.findAndUpdate(userBefore)) { user =>
      user shouldBe Some(userAfter)
    }
  }

  " find and remove by mobile " should " be return user which the mobile is " in {
    val mobile = 15001109204L
    (mockUserDAL.findAndRemove _) expects mobile returning Future(Some(User(mobile = Some(mobile)))) once ()
    whenReady(userService.findAndRemove(mobile)) { user =>
      user shouldBe Some(User(mobile = Some(mobile)))
    }
  }
}
