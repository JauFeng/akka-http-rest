package com.turing.rest

import com.turing.rest.common.config.DatabaseConfig

import reactivemongo.api._
import reactivemongo.api.commands.WriteConcern
import reactivemongo.core.nodeset.Authenticate

import scala.language.postfixOps

import scala.concurrent.duration._

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * MongoDB database object.
 */
object MongoDBDatabase extends DatabaseConfig {

  val driver = new MongoDriver

  /** Connection options. */
  val connectionOpts = MongoConnectionOptions(
    // canonical options - connect timeout milliseconds
    connectTimeoutMS = 0,

    // canonical options - authentication options
    authSource = None,
    sslEnabled = false,
    sslAllowsInvalidCert = false,
    authMode = CrAuthentication,

    // reactivemongo specific options
    tcpNoDelay = false,
    keepAlive = false,
    nbChannelsPerNode = 10, // 10 connections per node

    // read and write preferences
    readPreference = ReadPreference.primaryPreferred,
    writeConcern = WriteConcern.Default
  )

  /** Connection. */
  lazy val connection: MongoConnection = driver.connection(
    nodes = List(mongoDBUri),
    options = connectionOpts
  //    authentications = List(Authenticate(mongoDBDatabase, mongoDBUser, mongoDBPassword))
  )

  /** Connect fail over Strategy. */
  val failOverStrategy = FailoverStrategy(
    initialDelay = 500 milliseconds, // Initial delay between the first failed attempt and the next one.
    retries = 5,
    delayFactor = n => 1 // Function that takes the current iteration and return a factor to be applied to the initialDelay.

  )

  /** Database. */
  lazy val database: DefaultDB = connection(mongoDBDatabase, failoverStrategy = failOverStrategy)

  logger.info(
    s"""
       | Connect to database : $mongoDBDatabase success.
     """.stripMargin
  )
}
