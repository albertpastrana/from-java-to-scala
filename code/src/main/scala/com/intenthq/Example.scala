package com.intenthq

import scalaz._

import scala.util.Random
import scala.collection.mutable

object Example extends App {
  println("Hello scala world!")

  println("Going to show some cryptic syntax")
  val ok = \/-("That one worked")
  val fail = -\/("That one didn't work")
  val fiftyFifty = \/.fromTryCatchNonFatal(1 / Random.nextInt(2)).map(_.toString)

  ok :: fail :: fiftyFifty :: Nil foreach {
    case \/-(res) => println(s"It worked! -> $res")
    case -\/(err) => println(s"It didn't work -> $err")
  }

  println("Going to use a couple of classes")

  private def print(name: String, age: Int) :Unit =
    println(s"This person's name is $name and is $age")

  val p1 = new JPerson("Jane", 23)
  print(p1.getName, p1.getAge)
  val p2 = SPerson("John", 45)
  print(p2.name, p2.age)

  // copy gives you a very easy (and non-verbose) way to create
  // a new instance based on another one
  val p3 = new JPerson(p1.getName, 24) //imagine this with 6 parameters
  print(p3.getName, p3.getAge)
  val p4 = p2.copy(age = 46)
  print(p4.name, p4.age)

  println("You can write imperative Scala")
  val input = (1 to 10).toList
  val outputMut = new mutable.ListBuffer[Int]
  for (v <- input) {
    if (v % 2 == 0)
      outputMut.+=(v*3)
  }
  println(outputMut)

  println("Instead of idiomatic Scala")
  val outputImm = input.filter(_ % 2 == 0).map(_*3)
  println(outputImm)
}
