package com.intenthq.di

object Service extends App {

  println("""Let's execute the service using "classical" DI.""")
  val serviceOO = new ServiceOO(Dependency1)
  println(
    serviceOO.something(1)
  )

  println("Let's use now implicits")
  implicit val dep: Dependency = Dependency1
  println(
    ServiceImplicits.something(1)
  )

  println("Let's use functions")
  println(
    ServiceFunction.something(1, Dependency1.doStuff)
  )

}

class ServiceOO(dependency: Dependency) {
  def something(i: Int): String = s"Result is ${dependency.doStuff(i)}"
}

object ServiceImplicits {
  def something(i: Int)(implicit dependency: Dependency): String = s"Result is ${dependency.doStuff(i)}"
}

object ServiceFunction {
  def something(i: Int, dependency: Int => Int): String = s"Result is ${dependency(i)}"
}

trait Dependency {
  def doStuff(i: Int): Int
}

object Dependency1 extends Dependency {
  override def doStuff(i: Int): Int = i + 1
}
