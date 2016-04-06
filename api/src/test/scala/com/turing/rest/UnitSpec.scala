package com.turing.rest

import org.scalatest._

import org.scalamock.scalatest.MockFactory

/**
 *
 *
 * Created by sean on 16-1-26.
 */
trait UnitSpec extends FlatSpec with Matchers with OptionValues with Inside with Inspectors with MockFactory {
}
