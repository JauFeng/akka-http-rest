package com.turing.rest.user

import com.google.inject.Inject
import com.typesafe.scalalogging.StrictLogging

import scala.util.{Failure, Success, Try}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * User service trait.
 */
trait UserServiceComponent {
  /**
   * Find user by user's ID.
   *
   * @param userId user' id
   * @return [[com.turing.rest.user.User]]
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
  def findUsers(offset: String, limit: Int): Future[UserSeq]

  /**
   * Create user.
   *
   * @param user user object
   * @return
   */
  def create(user: User): Future[Either[Throwable, Int]]

  /**
   * Update user if find.
   *
   * @param user user object
   * @return
   */
  def findAndUpdate(user: User): Future[Option[User]]

  /**
   * Remove user if found.
   *
   * @param mobile user' mobile
   * @return
   */
  def findAndRemove(mobile: Long): Future[Option[User]]
}

/**
 * User service.
 */
class UserService @Inject() (userDAL: UserDALComponent) extends UserServiceComponent with StrictLogging {

  /**
   * Find user by user's ID.
   *
   * @param userId user' id
   * @return [[User]]
   */
  override def findUserById(userId: String): Future[Option[User]] = {
    userDAL.findUserById(userId)
  }

  /**
   * Find user by user's mobile.
   *
   * @param mobile user's mobile with is Primary Key.
   * @return
   */
  override def findUserByMobile(mobile: Long): Future[Option[User]] = {
    userDAL.findUserByMobile(mobile)
  }

  /**
   * Find users with offset and limit.
   *
   * @param offset offset
   * @param limit  limit default is 10
   * @return
   */
  override def findUsers(offset: String, limit: Int = 10): Future[UserSeq] = {
    userDAL.findUsers(offset, limit) match {
      case Success(f) => f map (users => UserSeq(users.size, users))
      case Failure(e) =>
        logger.error("Find users error: ", e)
        Future(UserSeq())
    }
  }

  /**
   * Create user.
   *
   * @param user user object
   * @return
   */
  override def create(user: User): Future[Either[Throwable, Int]] = {
    userDAL.create(user)
  }

  /**
   * Update user if find.
   *
   * @param user user object
   * @return
   */
  override def findAndUpdate(user: User): Future[Option[User]] = {
    userDAL.findAndUpdate(user)
  }

  /**
   * Remove user if found.
   *
   * @param mobile user' mobile
   * @return
   */
  override def findAndRemove(mobile: Long): Future[Option[User]] = {
    userDAL.findAndRemove(mobile)
  }
}

import com.google.inject.AbstractModule

class UserServiceDependencyModule extends AbstractModule {
  override def configure(): Unit = {
    bind[UserServiceComponent](classOf[UserServiceComponent]).to(classOf[UserService])
  }
}