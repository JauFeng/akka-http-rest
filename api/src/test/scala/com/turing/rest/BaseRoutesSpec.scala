package com.turing.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContextExecutor

/**
 * Base Routes for all routes spec should be implement.
 */
trait BaseRoutesSpec extends UnitSpec with ScalatestRouteTest {
  spec =>

  trait TestRoutesSupport {
    implicit val _system: ActorSystem = spec.system
    implicit val _materializer: ActorMaterializer = spec.materializer
    implicit val _executionContextExecutor: ExecutionContextExecutor = spec.executor

    //    val logger = NoLogging
  }

}
