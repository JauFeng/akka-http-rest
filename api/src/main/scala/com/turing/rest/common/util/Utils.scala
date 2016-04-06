package com.turing.rest.common.util

import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

import com.typesafe.scalalogging.StrictLogging

/**
 * Common utils.
 */
object Utils extends StrictLogging {
  /**
   * Encrypt password.
   *
   * @param password user's password
   * @return
   */
  def encryptPassword(password: String): String = {
    val iterations = 10000
    val chars = password.toCharArray
    val salt = getSalt

    val spec = new PBEKeySpec(chars, salt, iterations, 64 * 8)
    val hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(spec).getEncoded

    s"$iterations:${toHex(salt)}:${toHex(hash)}"
  }

  /**
   * Validate password.
   *
   * @param originalPassword plain password
   * @param storedPassword   password stored in somewhere(i.e. database)
   * @return
   */
  def validatePassword(originalPassword: String, storedPassword: String): Boolean = {
    try {
      val parts: Array[String] = storedPassword.split(":")
      val iterations: Int = parts(0).toInt
      val salt: Array[Byte] = fromHex(parts(1))
      val hash: Array[Byte] = fromHex(parts(2))

      val spec = new PBEKeySpec(originalPassword.toCharArray, salt, iterations, hash.length * 8)
      val skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
      val testHash = skf.generateSecret(spec).getEncoded

      var diff = hash.length ^ testHash.length
      val _hash: Array[(Byte, Byte)] = if (hash.length < testHash.length) hash zip testHash else testHash zip hash
      _hash foreach { t => diff |= t._1 ^ t._2 }
      diff == 0
    }
    catch {
      case e: Exception =>
        logger.error(s"Perhaps, you didn't encrypt your password: ${e.getMessage}", e)
        false
    }
  }

  /**
   * Salt for encrypt.
   *
   * @return
   */
  def getSalt: Array[Byte] = {
    val salt: Array[Byte] = Array.fill[Byte](16)(0)
    SecureRandom.getInstance("SHA1PRNG").nextBytes(salt)
    salt
  }

  /**
   * Array[Byte] to Hex String.
   *
   * @param byteArray byte array
   * @return
   */
  def toHex(byteArray: Array[Byte]): String = {
    val hex = BigInt(1, byteArray).toString(16)

    val paddingLength = (byteArray.length * 2) - hex.length
    if (paddingLength > 0)
      s"0${paddingLength}d$hex"
    else
      hex
  }

  /**
   * Hex String to Array[Byte].
   *
   * @param hex hex string
   * @return
   */
  def fromHex(hex: String): Array[Byte] = {
    val byteArray: Array[Byte] = Array.fill[Byte](hex.length / 2)(0)
    val n = byteArray.indices.toIterator
    var index = 0
    byteArray map { byte =>
      index = n.next()
      Integer.parseInt(hex.substring(2 * index, 2 * index + 2), 16).toByte
    }
  }
}
