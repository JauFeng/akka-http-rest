package com.turing.rest.user

import com.google.inject.Guice
import com.turing.rest.UnitSpec

import org.scalatest.concurrent.ScalaFutures._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Right, Success}
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * User DAL Unit Test.
 */
class UserDALSpec extends UnitSpec {
  val injector = Guice.createInjector(new UserDALDependencyModule)
  val userDAL = injector.getInstance[UserDAL](classOf[UserDAL])

  val mobile = 15001109204L
  val user = User(mobile = Some(mobile), name = Some("sean"), password = Some("pwd"),
    email = Some("freecloud@gmail.com"), gender = Some("female"), age = Some(20))

  " create user " should " be return Right(1) " in {
    //    whenReady(userDAL.create(user)) { either =>
    //      either shouldBe Success(Right(1))
    //    }
  }

  " find user by mobile " should " be return user which the mobile is " in {
    //    whenReady(userDAL.findUserByMobile(mobile)) { user =>
    //      user shouldBe Some(user)
    //    }
  }

  " find users " should " be return users limit 5 " in {
    //    whenReady(userDAL.findUsers(offset = "", limit = 5).get) { users =>
    //      users.size should be <= 5
    //    }
  }

  " find and update by mobile " should " be return user which the mobile is " in {
    //    val userUpdate = user.copy(name = Some("_sean"), password = Some("_pwd"), email = Some("freecloud@gmail.com"),
    //      gender = Some("male"), age = Some(15))
    //    val f: Future[Option[User]] = userDAL.findAndUpdate(userUpdate)
    //    val r: Option[User] = Await.result(f, Duration.Inf)
    //    r.get should have(
    //      'name (Some("_sean")),
    //      'password (Some("_pwd")),
    //      'email (Some("freecloud@gmail.com")),
    //      'gender (Some("male")),
    //      'age (Some(15))
    //    )
  }

  " find and remove by mobile " should " be return user which the mobile is " in {
    //    whenReady(userDAL.findAndRemove(mobile)) { user =>
    //      user.get.mobile shouldBe mobile
    //    }
  }
}
