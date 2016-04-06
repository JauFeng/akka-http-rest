package com.turing.rest.common.config

import com.typesafe.scalalogging.StrictLogging

/**
 * Database's config.
 */
trait DatabaseConfig extends BaseConfig with StrictLogging {
  /**
   * MongoDB configs.
   */
  private val DATABASE_MONGODB_URI = "database.mongoDB.uri"
  private val DATABASE_MONGODB_USER = "database.mongoDB.user"
  private val DATABASE_MONGODB_PWD = "database.mongoDB.password"
  private val DATABASE_MONGODB_DB = "database.mongoDB.db"

  lazy val mongoDBUri = config.getString(DATABASE_MONGODB_URI)
  lazy val mongoDBUser = config.getString(DATABASE_MONGODB_USER)
  lazy val mongoDBPassword = config.getString(DATABASE_MONGODB_PWD)

  // User `-Ddatabase.mongoDB.db=[ dev | pro ]` set MongoDB database.
  private val mongoDBSys = System.getProperty(DATABASE_MONGODB_DB)
  private val mongoDBDefault = config.getString(DATABASE_MONGODB_DB)
  lazy val mongoDBDatabase = Option(mongoDBSys).getOrElse(mongoDBDefault)

  logger.info(
    s"""
       | MongoDB URI : $mongoDBUri
       | MongoDB Database : $mongoDBDatabase
     """.stripMargin
  )

  /**
   * H2 database configs.
   */
  private val DATABASE_H2DB_URI = "database.h2.uri"

  lazy val h2DBUri = config.getString(DATABASE_H2DB_URI)
}
