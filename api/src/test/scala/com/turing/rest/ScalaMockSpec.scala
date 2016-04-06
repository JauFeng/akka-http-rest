package com.turing.rest

import com.turing.rest.user.User
import org.scalatest.concurrent.ScalaFutures._
import org.scalatest.time.{Millis, Seconds, Span}

import scala.util.{Success, Try}
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class ScalaMockSpec extends UnitSpec {

  trait MyTrait {
    def setPosition(x: Int, y: Int): Int

    def getPosition(): Int
  }

  " function mock " should " like this " in {
    val func = mockFunction[Int, String]

    inAnyOrder {
      func expects 1 returning "1"
    }

    func(1) === "1"
  }

  " trait mock " should " like this " in {
    val m = mock[MyTrait]

    inSequence {
      (m.setPosition _) expects (1, 2) returning 3
      (m.setPosition _) expects (2, 2) returning 4
      (m.getPosition _) expects () returning 1
    }

    (m.setPosition(1, 2) === 3) && (m.setPosition(2, 2) === 4) && (m.getPosition() === 1)
  }

}
