package com.turing.rest.common

final case class StatusWrapper(status: String = "OK", token: Option[String] = None)