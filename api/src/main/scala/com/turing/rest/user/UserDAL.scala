package com.turing.rest.user

import com.typesafe.scalalogging.StrictLogging
import reactivemongo.bson._
import reactivemongo.api.commands.{UpdateWriteResult, WriteResult}
import reactivemongo.api.collections.bson.BSONCollection

import scala.concurrent.Future
import scala.util.Try

/**
 * User database access library trait.
 */
trait UserDALComponent {

  /**
   * User collection which name is `patient` in Mongo DB.
   */
  val collection: BSONCollection

  /**
   * Find user by user's id.
   *
   * @param userId user's id
   * @return
   */
  def findUserById(userId: String): Future[Option[User]]

  /**
   * Find user by user's mobile.
   *
   * @param mobile user's mobile with is Primary Key.
   * @return
   */
  def findUserByMobile(mobile: Long): Future[Option[User]]

  /**
   * Find users with offset and limit.
   *
   * @param offset offset
   * @param limit  limit default is 10
   * @return
   */
  def findUsers(offset: String, limit: Int): Try[Future[List[User]]]

  /**
   * Create user.
   *
   * @param user user object
   * @return
   */
  def create(user: User): Future[Either[Throwable, Int]]

  /**
   * Update user.
   *
   * @param user user object
   * @return
   */
  def update(user: User): Future[Either[Throwable, Int]]

  /**
   * Update user if find.
   *
   * @param user user object
   * @return
   */
  def findAndUpdate(user: User): Future[Option[User]]

  /**
   * Delete user.
   *
   * @param userId user's id
   * @return
   */
  def remove(userId: String): Future[Either[Throwable, Int]]

  /**
   * Remove user if found.
   *
   * @param mobile user's mobile
   * @return
   */
  def findAndRemove(mobile: Long): Future[Option[User]]
}

import com.turing.rest.MongoDBDatabase._

import scala.concurrent.ExecutionContext.Implicits.global

import UserReaderAndWriter._

class UserDAL extends UserDALComponent with StrictLogging {
  /**
   * User collection which name is `patient` in Mongo DB.
   */
  override val collection: BSONCollection = database.collection[BSONCollection]("patient")

  /**
   * Find user by user's id.
   *
   * @param userId user's id
   * @return
   */
  override def findUserById(userId: String): Future[Option[User]] = {
    val query = BSONDocument("_id" -> BSONObjectID(userId))

    collection.find[BSONDocument](query).one[User]
  }

  /**
   * Find user by user's mobile.
   *
   * @param mobile user's mobile with is Primary Key.
   * @return
   */
  override def findUserByMobile(mobile: Long): Future[Option[User]] = {
    val query = BSONDocument("mobile" -> BSONLong(mobile))

    collection.find[BSONDocument](query).one[User]
  }

  /**
   * Find users with offset and limit.
   *
   * @param offset offset
   * @param limit  limit default is 10
   * @return
   */
  override def findUsers(offset: String, limit: Int = 10): Try[Future[List[User]]] =
    Try {
      val selector = if (offset.trim.nonEmpty)
        BSONDocument("_id" -> BSONDocument("$lt" -> BSONObjectID(offset)))
      else BSONDocument()
      val sort = BSONDocument("_id" -> -1)

      collection
        .find(selector)
        .sort(sort)
        .cursor[User]().collect[List](maxDocs = limit, stopOnError = true)
    }

  /**
   * Create user.
   *
   * @param user user object
   * @return
   */
  override def create(user: User): Future[Either[Throwable, Int]] = {
    collection.insert[User](user) map {
      case writeResult: WriteResult if writeResult.ok => Right(writeResult.n)
      case writeResult: WriteResult if writeResult.hasErrors =>
        logger.error(s"${this.getClass} create user error : ${writeResult.errmsg}")
        Left(new Exception(writeResult.errmsg.getOrElse("<None>")))
    }
  }

  /**
   * Update user.
   *
   * @param user user object
   * @return
   */
  override def update(user: User): Future[Either[Throwable, Int]] = {
    val selector = BSONDocument("_id" -> user.id)

    val update = BSONDocument("$set" -> BSONDocument("age" -> user.age))

    collection.update(selector, update,
      upsert = false, // if no existing, insert
      multi = false // update multiple
    ) map {
      case updateWriteResult: UpdateWriteResult if updateWriteResult.ok =>
        Right(updateWriteResult.nModified)
      case updateWriteResult: UpdateWriteResult if updateWriteResult.hasErrors =>
        logger.error(s"${this.getClass} update user error : ${updateWriteResult.errmsg}")
        Left(new Exception(updateWriteResult.errmsg.getOrElse("<None>")))
    }
  }

  /**
   * Update user if find.
   *
   * @param user entire user object
   * @return
   */
  override def findAndUpdate(user: User): Future[Option[User]] = {
    val selector = BSONDocument("mobile" -> BSONLong(user.mobile.getOrElse(0L)))

    collection.findAndUpdate[BSONDocument, User](
      selector, user, fetchNewObject = true, upsert = false
    ).map(_.result[User])
  }

  /**
   * Remove user by user id.
   *
   * @param userId user's id
   * @return
   */
  override def remove(userId: String): Future[Either[Throwable, Int]] = {
    val selector = BSONDocument("_id" -> BSONObjectID(userId))

    collection.remove[BSONDocument](selector, firstMatchOnly = true) map {
      case writeResult: WriteResult if writeResult.ok =>
        Right(writeResult.n)
      case writeResult: WriteResult if writeResult.hasErrors =>
        Left(new Exception(writeResult.errmsg.getOrElse("<None>")))
    }
  }

  /**
   * Remove user if found.
   *
   * @param mobile user's mobile
   * @return
   */
  override def findAndRemove(mobile: Long): Future[Option[User]] = {
    val selector = BSONDocument("mobile" -> BSONLong(mobile))

    collection findAndRemove selector map (_.result[User])
  }

}

import com.google.inject.AbstractModule

class UserDALDependencyModule extends AbstractModule {
  override def configure(): Unit = {
    bind[UserDALComponent](classOf[UserDALComponent]).to(classOf[UserDAL])
  }
}