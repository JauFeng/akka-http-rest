package com.turing.rest.common.config

/**
 * HTTP server's configs.
 */
trait ServerConfig extends BaseConfig {
  private val SERVER_HOST = "server.host"
  private val SERVER_PORT = "server.port"

  lazy val serverHost = config.getString(SERVER_HOST)
  lazy val serverPort = config.getInt(SERVER_PORT)
}
