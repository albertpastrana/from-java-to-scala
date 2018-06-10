---
title: From OO Java to FP Scala
theme: moon
revealOptions:
    transition: none
    controlBackArrows: faded
    progress: false
    slideNumber: c/t
    showSlideNumber: speaker
---

# From OO Java to FP Scala

## Epic wins and epic fails

by Albert Pastrana @ [Intent HQ](https://www.intenthq.com)

---
# What this talk is about
---
### What worked for us
### What didn't work for us <!-- .element: class="fragment" -->
Note: a bit like a retrospective
---
## What this talk is
# NOT
## about
---
### Good OO practices
### Good FP practices
### What to do (or how to do it)
### Scala vs Java

Note: I'm not trying to say how anybody should program, what to do or anything. I'm not trying to compare Scala and Java either. I have my preferences and opinion. We had our preferences and opinions. We made our choices, good and bad. You can make your own.
---
### This talk based in our
## experience and opinion
---
## A bit of context
Our first Scala commit: May, 2014  
First Java 8 release: March, 2014  
We were stuck with Java 7  
We started with Scala 2.10 (although Scala 2.11 was already there)

Note: that means some of the considerations would be different nowadays
---
# Find a motivation for using Scala
---
## “We can enjoy developing more and be more productive using Scala”
---
# Think of other reasons to use Scala
---
### JVM language
### "Enforces" immutability
### Multi-paradigm language (OO & FP)
### More concise or expressive
### Pattern matching, traits, type inference

and string interpolation, currying, lazy evaluation, singleton objects, for comprehensions, "pimp my library", nested methods, named arguments, default & by-name parameters...  <!-- .element: class="fragment small" -->
---
## Think of good reasons
# not
## to use scala
---
### My team is java
### Java8
### Difficult to master
### Compilation time

Note: tbh, we didn't think of all of them beforehand
---
# Epic Wins
## (and Fails)
---
<!-- ## Epic Win #X -->
## Introduce it slowly (and safely!)
Java and Scala can interoperate seamingly and you can use your Java libraries from Scala.

We started by writing any new tests in Specs2 instead of JUnit, we found a safe place were we all were able to use and learn Scala.
---
### Side effect: our tests looked much better
```java
@Test
public void testOnePlusOneEqualsTwo() {
  assertEquals(1 + 1, 2)
}

@Test
public void testAssertThatEveryItemContainsString() {
  assertThat(Arrays.asList(new String[] { "fun", "ban", "net" }),
    everyItem(containsString("n")));
}
```
vs
```scala
"1 + 1 = 2" >> {
  1 + 1 must_=== 2
}

"every item contains 'n'" >> {
  List("fun", "ban", "net").map { _ must contain("n") }
}
```
---
## Don't rewrite existing code
Given that you can mix Java and Scala, after the tests, we started adding production code in Scala. That way the transition went smoothly.
---
### for example, using case classes
---
```java
public class JPerson {

    private String name;
    private int age;

    public JPerson(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JPerson jPerson = (JPerson) o;
        return age == jPerson.age &&
                java.util.Objects.equals(name, jPerson.name);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, age);
    }
}
```
---

```scala
case class SPerson(name: String, age: Int)
```
![well done](assets/easy-peasy.gif) <!-- .element: class="fragment" -->
---
## Use Scala in a greenfield project
We had a new project we had to start from scratch where we were going to use Spark.
It was the perfect project to introduce Scala.
---
## (Most of) The team was motivated in using Scala
This is actually one of the most important questions we asked ourselves. Only consider such a change if you have buy-in from your team.
---
## Hiring somebody with Scala experience
Having somebody to coach the team and help us setting up the project was incredibly helpful.
---
## Hiring somebody with Scala experience
Need to tell them beforehand about their role (e.g. will get asked many questions). Also, they may not be able to do things exactly as they would like to.

If you don't do it, they can get burned.
---
## Embrace immutability
Among others, because it makes it easier to write, read and reason about the code.

And we have less bugs.<!-- .element: class="fragment" -->
---
## Pure functions, side effects, error handling
Similarly to the previous point, having pure functions and modeling errors as values (instead of exceptions) made our code easier to understand and test.

And we have less bugs.<!-- .element: class="fragment" -->
---
## We didn't think about some of the drawbacks of Scala
Upgrades are not binary compatible  
Upgrade process (always behind Java)  
Battery life  
Compilation time  
---
<asciinema-player src="assets/scalac.cast" font-size="medium"></asciinema-player>
---
## The choice is yours
Scala is flexible enough so that you can solve the same problem using different solutions.

Let's see an example: dependency injection
---
### Regular OO approach
```scala
class ServiceOO(dep: Dependency) {
  def something(i: Int): String =
    s"Result is ${dep.doStuff(i)}"
}

trait Dependency {
  def doStuff(i: Int): Int
}

object Dependency1 extends Dependency {
  override def doStuff(i: Int): Int = i + 1
}

object Service extends App {
  val serviceOO = new ServiceOO(Dependency1)
  println(serviceOO.something(1))
}
```
Note: of course, you could use a framework to initialise the instances and everything
---
### Implicits magic
```scala
object ServiceImplicits {
  def something(i: Int)(implicit dep: Dependency): String =
    s"Result is ${dep.doStuff(i)}"
}

trait Dependency {
  def doStuff(i: Int): Int
}

object Dependency1 extends Dependency {
  override def doStuff(i: Int): Int = i + 1
}

object Service extends App {
  implicit val dep: Dependency = Dependency1
  println(ServiceImplicits.something(1))
}
```
---
### Just use functions
```scala
object ServiceFunction {
  def something(i: Int, dependency: Int => Int): String =
    s"Result is ${dependency(i)}"
}

object Dependency1 {
  override def doStuff(i: Int): Int = i + 1
}

object Service extends App {
  println(ServiceFunction.something(1, Dependency1.doStuff))
}
```
---
### Testing them is really easy
```scala
  "functions style" >> {
    ServiceFunction.something(3, i => i) must_=== "Result is 3"
  }
```
---
## Sometimes you have too many choices
This can lead to inconsistent solutions, projects more difficult to understand and an entry barrier for newcomers.
---
### For example
```scala
val input = (1 to 10).toList

// You can use imperative Scala
val outputMut = new mutable.ListBuffer[Int]
for (v <- input) {
  if (v % 2 == 0)
    outputMut.+=(v*3)
}

// Instead of idiomatic Scala
val outputImm = input.filter(_ % 2 == 0).map(_*3)
```
---
## Scala is not Haskell
The team needs to agree on the level of functional programming they want to reach. And when they want to reach it.

FP purism vs pragmatism needs to be valued.

Note: in our case we tried to run before we could walk.
---
## Use Scalaz
Scalaz is a wonderful library but...  

Our team was not ready  
Its syntax can be scary  
Documentation could have been better  
Community felt a bit unwelcoming to newbies  

Note: this was our impression when we started using scala (May, 2014), scalaz project has matured and changed a lot since then. Cats also appeared as an alternative worth considering.
---
syntax example
```scala
  val ok = \/-("That one worked")
  val fail = -\/("That one didn't work")
  val fiftyFifty = \/.fromTryCatchNonFatal(1 / Random.nextInt(2)).map(_.toString)

  ok :: fail :: fiftyFifty :: Nil foreach {
    case \/-(res) => println(s"It worked! -> $res")
    case -\/(err) => println(s"It didn't work -> $err")
  }
```

Note: sometimes it's better to find (or create!) small libraries that do what you need
---
answer example
![example of harsh answer in scalaz](assets/example-harsh-answer-scalaz.png)
---
## Better hires
Given that it was difficult to find people with Scala experience, we stopped focusing on specific technical skills and focused more on the people themselves.
---
## Difficulty finding people
Specially difficult to work with recruiters when it's a "new technology" and they are used to find people by keyword.
---
# What's it going
# to be?
---
![choice](assets/choice.jpg)
---
# TL;DR
#### in general pros outweighted cons
#### we are happier
#### we develop faster
#### we have less bugs => more stable platform
#### we invest more in people  
#### we hire different/better  

Note: also, be prepared to spend time training
---
# Q&A

<p>You can find the talk and the code that goes with it at https://github.com/albertpastrana/from-java-to-scala </p><!-- .element: class="fragment" -->