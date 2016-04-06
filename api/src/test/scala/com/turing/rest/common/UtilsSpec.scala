package com.turing.rest.common

import com.turing.rest.UnitSpec
import com.turing.rest.common.util.Utils

/**
 * Created by sean on 16-4-2.
 */
class UtilsSpec extends UnitSpec {
  " encrypt password " should " take into account the password " in {
    val password1 = "password1"
    val password2 = "password2"

    val encryptPWD1 = Utils.encryptPassword(password1)
    val encryptPWD2 = Utils.encryptPassword(password2)

    info(s"$password1 encrypted is: $encryptPWD1")
    info(s"$password2 encrypted is: $encryptPWD2")

    encryptPWD1 should not be encryptPWD2
  }

  " validate password " should " original password == encrypt password " in {
    val password1 = "password1"
    val password2 = "password2"

    val encryptPWD1 = Utils.encryptPassword(password1)
    val encryptPWD2 = Utils.encryptPassword(password2)

    Utils.validatePassword(password1, encryptPWD1) should be(true)

    Utils.validatePassword(password2, encryptPWD2) should be(true)
  }
}