package com.turing.rest

import com.google.inject.Guice
import com.turing.rest.authentication.{AuthenticationServiceDependencyModule, AuthenticationService}
import com.turing.rest.user.{UserService, UserServiceDependencyModule, UserDALDependencyModule}

/**
 * Create all dependency modules of guice.
 */
trait Bean {
  /**
   * Injector
   */
  val injector = Guice createInjector (
    new UserDALDependencyModule,
    new UserServiceDependencyModule,
    new AuthenticationServiceDependencyModule
  )

  /** User service. */
  lazy val userService: UserService = injector.getInstance[UserService](classOf[UserService])

  /** Authentication Service. */
  lazy val authenticationService: AuthenticationService = injector.getInstance[AuthenticationService](classOf[AuthenticationService])
}
