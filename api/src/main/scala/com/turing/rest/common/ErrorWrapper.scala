package com.turing.rest.common

/**
 * Error response Json case class.
 *
 * @example {{{
 *            {
 *             statusCode : 404,
 *             errMsg : "Not Found",
 *             userMsg : ""
 *            }
 *          }}}
 * @param statusCode HTTP status code
 * @param errMsg     error messages
 * @param userMsg    custom messages
 */
final case class ErrorWrapper(
  statusCode: String = "OK",
  errMsg: Option[String] = None,
  userMsg: Option[String] = None
)
