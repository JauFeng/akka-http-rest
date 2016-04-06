package com.turing.rest.user

import com.typesafe.scalalogging.StrictLogging

import reactivemongo.bson.BSONObjectID

/**
 * Case class User.
 */
final case class User(
  id: Option[String] = None,
  mobile: Option[Long] = None,
  name: Option[String] = None,
  password: Option[String] = None,
  email: Option[String] = None,
  gender: Option[String] = None,
  age: Option[Int] = None,

  login: Option[String] = None,
  loginLowerCased: Option[String] = None
)

import reactivemongo.bson._

object UserReaderAndWriter extends StrictLogging {

  /** User Reader of BSONDocument Reader */
  implicit object UserReader extends BSONDocumentReader[User] {
    override def read(bson: BSONDocument): User = {
      val id = bson.getAs[BSONObjectID]("_id").map[String](_.stringify)
      val mobile = bson.getAs[Long]("mobile")
      val name = bson.getAs[String]("name")
      val password = bson.getAs[String]("password")
      val email = bson.getAs[String]("email")
      val gender = bson.getAs[String]("gender")
      val age = bson.getAs[BSONValue]("age") match {
        case Some(b) => b match {
          case BSONInteger(i) => Some(i)
          case BSONLong(l) => Some(l.toInt)
          case BSONDouble(d) => Some(d.toInt)
        }
        case None => None
      }

      User(id, mobile, name, password, email, gender, age)
    }
  }

  /** User Writer of BSONDocument Writer */
  implicit object UserWriter extends BSONDocumentWriter[User] {
    override def write(user: User): BSONDocument = {
      BSONDocument(
        "mobile" -> user.mobile.map[BSONLong](BSONLong),
        "name" -> user.name.map[BSONString](BSONString),
        "password" -> user.password.map[BSONString](BSONString),
        "email" -> user.email.map[BSONString](BSONString),
        "gender" -> user.gender.map[BSONString](BSONString),
        "age" -> user.age.map[BSONInteger](BSONInteger)
      )
    }
  }

}