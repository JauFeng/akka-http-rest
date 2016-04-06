package com.turing.rest

import java.util.Locale

import akka.actor.ActorSystem

import akka.stream.ActorMaterializer

import akka.http.scaladsl.Http

import com.typesafe.scalalogging.StrictLogging

import com.turing.rest.common.config.{DatabaseConfig, ServerConfig}

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

/**
 * The main object of application.
 */
object Main extends App with Bean with Routes with ServerConfig with DatabaseConfig with StrictLogging {
  // Set default local
  Locale.setDefault(Locale.US)

  // Configs.
  override implicit val _system: ActorSystem = ActorSystem("main-actor")
  override implicit val _materializer: ActorMaterializer = ActorMaterializer()
  override implicit val _executionContextExecutor: ExecutionContextExecutor = _system.dispatcher

  // HTTP Binding.
  val bindingFuture = Http().bindAndHandle(
    handler = routes,
    interface = serverHost,
    port = serverPort
  )

  // Binding handler.
  bindingFuture onComplete {
    case Success(binding) =>
      logger.info(s"HTTP Server started on $serverHost:$serverPort ... ")
      sys.addShutdownHook {
        binding.unbind()
        _system.terminate()
        logger.info("Server stopped.")
      }
    case Failure(error) =>
      logger.error(s"Can't start server on $serverHost:$serverPort", error)
      sys.addShutdownHook {
        _system.terminate()
        logger.info("Server stopped. ")
      }
  }
}