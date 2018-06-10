package com.intenthq

import org.specs2.Specification
import org.specs2.mutable

class ExampleSpec extends mutable.Specification {
  "1 + 1 = 2" >> {
    1 + 1 must_=== 2
  }

  "every item contains 'n'" >> {
    List("fun", "ban", "net").map { _ must contain("n") }
  }
}

class Example2Spec extends Specification { def is = s2"""
  ${1 + 1 must_=== 2}
  "every item contains 'n' $e1
  """

  private def e1 = List("fun", "ban", "net").map { _ must contain("n") }
}