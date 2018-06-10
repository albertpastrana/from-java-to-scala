package com.intenthq.di

import org.specs2.mutable.Specification

class ServiceSpec extends Specification {

  private def func(i: Int) = i

  "OO style" >> {
    val dep = new Dependency {
      override def doStuff(i: Int): Int = func(i)
    }
    new ServiceOO(dep).something(3) must_=== "Result is 3"
  }

  "implicits style" >> {
    implicit val dep: Dependency = (i: Int) => func(i)
    ServiceImplicits.something(3) must_=== "Result is 3"
  }

  "functions style" >> {
    ServiceFunction.something(3, func) must_=== "Result is 3"
  }

}
