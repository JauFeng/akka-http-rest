package com.turing.rest

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.ExecutionContextExecutor

/**
 * Base Routes for every routes have to extends.
 */
trait BaseRoutes extends StrictLogging {

  implicit val _system: ActorSystem
  implicit val _materializer: ActorMaterializer
  implicit val _executionContextExecutor: ExecutionContextExecutor

}
