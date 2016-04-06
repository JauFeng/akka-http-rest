package com.turing.rest.common.config

import java.io.File

import com.typesafe.config.{ConfigFactory, Config}

import com.typesafe.scalalogging.StrictLogging

/**
 * Base config aggregate server config and database config.
 */
trait BaseConfig {
  def config: Config = BaseConfig.config
}

object BaseConfig extends StrictLogging {
  private val CONFIG_FILE = "config.file"
  private val DEFAULT_CONFIG_FILE = "api/src/main/resources/application.conf"

  logger.info("Loading config file ... ")

  // User `-Dconfig.file=src/main/resources/application.conf` set config file src.
  private val prop = System.getProperty(CONFIG_FILE)
  private val configFile = Option(prop).getOrElse(DEFAULT_CONFIG_FILE)
  // You can also use a simple way: `val config = ConfigFactory.load()`
  private val config = ConfigFactory.load(ConfigFactory.parseFileAnySyntax(new File(configFile)))

  logger.info(s"Loaded $configFile file's configs. ")
}
