package com.turing.rest.authentication

import java.security.SecureRandom

import com.google.inject.Inject

import com.turing.rest.common.util.Utils
import com.turing.rest.user.UserDALComponent

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Authentication service trait.
 */
trait AuthenticationServiceComponent {

  /**
   * Register.
   *
   * @param registrationForm registration form.
   * @return
   */
  def register(registrationForm: RegistrationForm): Future[AuthenticationResult]

  /**
   * Login.
   *
   * @param loginForm login form.
   * @return
   */
  def login(loginForm: LoginForm): Future[AuthenticationResult]
}

/**
 *
 */
class AuthenticationService @Inject() (userDAL: UserDALComponent) extends AuthenticationServiceComponent {
  /**
   * Register.
   *
   * @param registrationForm registration form.
   * @return
   */
  override def register(registrationForm: RegistrationForm): Future[AuthenticationResult] = {
    // TODO: Register
    Future.successful(AuthenticationResult.Success(""))
  }

  /**
   * Login.
   *
   * @param loginForm login form.
   * @return
   */
  override def login(loginForm: LoginForm): Future[AuthenticationResult] = {
    userDAL.findUserByMobile(loginForm.mobile.toLong).flatMap {
      case Some(user) if Utils.validatePassword(loginForm.password, user.password.getOrElse("")) =>
        Future.successful(AuthenticationResult.Success(token = generateToken))
      case Some(_) =>
        Future.successful(AuthenticationResult.InvalidData("Password is invalid."))
      case None =>
        Future.successful(AuthenticationResult.MobileNotExists("Mobile doesn't exist. "))
    }
  }

  private lazy val random = new SecureRandom()

  private def generateToken = BigInt(255, random).toString(32)

}

import com.google.inject.AbstractModule

class AuthenticationServiceDependencyModule extends AbstractModule {
  override def configure(): Unit = {
    bind[AuthenticationServiceComponent](classOf[AuthenticationServiceComponent]).to(classOf[AuthenticationService])
  }
}