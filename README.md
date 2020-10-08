# Functional programming in Java

## Contents

0. [Closure](src/main/java/org/example/step0/Closure.java) is a reminder for the idea of closures, which is a corner stone for monadic computations
1. [ADT and Pattern Matching](src/main/java/org/example/step1/AdtAndPatternMatching.java) demonstrates how to define Algebraic Data Types (ADT) and pattern matching over them
2. [Mapper over ADTs](src/main/java/org/example/step2/MapOverContainers.java) demonstrates a naive approach to define a set of mappers over ADTs
3. [HKT and Functors](src/main/java/org/example/step3/Functors.java) introduces the notion of Lightweight Higher-Kinded polymorphism (aka. Higher-Kinded Types, HKT) and defines the notion of a functor
4. [Monads](src/main/java/org/example/step4/Monads.java) demonstrates the idea of monads
5. [Monadic parser combinators](src/main/java/org/example/step5/Parsers.java) defines a set of monadic parser combinators
6. [Real world parser combinators example](src/main/java/org/example/step6/RealWorldParsers.java) defines a monadic JSON parser that is built from the monadic parser combinators

## Closures

A closure is a function that comes with the environment where the function is lexically defined. The environment allows closures accessing variables outside the function's body. This is an important technic for monadic computations.

## Algebraic Data Types

Algebraic data type (ADT) is a way to define composite types. "Algebraic" here means that a structure of such a type is defined using algebraic operations "sum" and "product":

- "sum" is a structure that holds a value of one of types that are included into the sum type. It is remotely similar to enum in java.
- "product" is a structure that holds a combination of values of all the types that are included into the product type. It is similar to a class in java.

### Pattern matching

Algebraic data types are defined with structures that can be naturally decomposed. This opens the possibility to implement pattern matching. A pattern matching allows to peek into ones structure, extract its components and use them in further processing.

## Mappings over ADT

ADT can be viewed as a container (e.g. Id, Optional, List, Tree) for values, so it's only natural to express the desire to define uniform approach to modify the content of an ADT without breaking its structure. First thing that comes to mind is the polymorphism based on overloaded functions:
```java
class MapperUtil {
  static <A, B> Id<B> map(Function<A, B> fun, Id<A> id) { ... }

  static <A, B> List<B> map(Function<A, B> fun, List<A> as) { ... }

  static <A, B> Maybe<B> map(Function<A, B> fun, Optional<A> m) { ... }

  static <A, B> Tree<B> map(Function<A, B> fun, Tree<A> m) { ... }
}
```
All the versions of the overloaded function `map` serves the same purpose:
they convert values which are wrapped in a container from one type to
another without altering the structure of the container.
All the signatures of the overloaded function look the almost same, they
all accept a function from `A` to `B`, but they differ on the second
argument: the first one accepts `Id<A>`, the second one accepts `List<A>`,
the third one accepts `Optional<A>` and the forth one accepts `Tree<A>`.
Notice that all the functions are parameterized over some polymorphic type `A`.
The difference is what comes before `A`. This part is called **type constructor**.

This solution does its job, but on the type level there are no way to state the fact that there are complimentary mappers for the containers. In order to generalize the idea of mappings and state the fact that there is a complimentary map function for every container, the interface-based *ad hoc* polymorphism comes to rescue. Unfortunately, it's illegal to define the interface like this:

```java
interface Functor<F> {
  <A, B> F<B> map(Function<A, B> fun, F<A> cont); // <<-- Error: Type 'F' does not have type parameters.
}
```

## Higher-Kinded types and Functors
Higher-kinded polymorphism (or higher-kinded types, HKT) is a means to generalize over type constructors.

### Type constructors
#### Kinds
A type constructor is a feature of a programming language that allows one building new types from old ones. Java implements this idea using generics.
The number of type parameters defines the arity of their type constructor. The
arity splits the set of all type constructors into equivalence classes.
Such equivalence classes are called *kinds*. A type constructor without any
type parameters (for example, `Integer`) is denoted as `*`, a type constructor
with a single type parameter (for example, `List`) is denoted as `* -> *`,
a type constructor with two type parameters (for example, `Map`) is denoted
as `* -> * -> *`, and so on.

#### Relation to data constructors
Type constructors differ from *data constructors* (regular java constructors)
in the context they are applicable to: whereas data constructors are called
during a program's execution, they build new values and define what the values
are like, type constructors are meant to create new types during a
program's compilation.

### Lightweight HKT
There is a way to bring the notion of HKT to languages which lack HKT.
For this to be possible a number of new abstractions needs to be introduced.

#### The App interface
The `App` interface encodes the notion of `F<A>`, where both `F` and `A`
are polymorphic:
```java
interface App<F, A> {}
```
The `Functor` interface from above can be transformed into a legal
java construction using the `App` interface:
```java
interface Functor<F> {
  <A, B> App<F, B> map(Function<A, B> fun, App<F, A> value);
}
```

#### Injections and projections
Injections and projection are meant to perform legal conversions from `App<F, A>` to `F<A>` and vice versa.

##### Injection
`F<A>` needs to become a subtype of `App<F, A>`, but the declaration is a bit
tricky. Let's start with the simplest possible container: `Id`. Semantically
`Id<T>` is the same as `T`:

```java
class Id<T> implements App<Id.mu, T> {
  T value;
  public Id(T value) {
    this.value = value;
  }
  public static final class mu {}
}
```
Here the class `Id.mu` is passed into `App` instead of passing `Id`, because
`Id` is generalized over `T` and `Id.mu` is not generalized, simply passing `Id` will trigger the `rawtypes` warning.
Java inheritance rules allow passing an instance of `Id<T>` everywhere where `App<Id.mu, T>` is
expected. This way defines the conversion from `F<A>` to `App<F, A>`.

##### Projection

The conversion from `App<F, A>` to `F<A>` is done by performing a downcast:
```java
class Id<T> extends App<Id.mu, T> {
    // the field and constructor omitted

    public static final class mu {}

    static<T> Id<T> narrow(App<Id.mu, T> value) {
        return (Id<T>) value;
    }
}
```
So far the polymorphic container `Id` can be used like this:
```java
App<Id.mu, Integer> appIdInt = new Id<>(42);
Id<Integer> idContainer = Id.narrow(appIdInt);
assert idContainer.value == 42;
```

##### Kind class and marker class

The class that implements `App<F, A>` is usually called a `Kind` class and its name contains the `Kind` suffix.

The kind class also defines a marker class which is usually called `mu`.
Every polymorphic container has to define its own marker class `mu`.

## Monads

A monad is ~~just a monoid in the category of endofunctors~~ a design pattern in functional languages. It's a generalization of a container. In order for a container to become a monad it needs to implement two methods:
- `pure` is a way to put a value into the container
- `flatMap` allows building monadic computations

Monads make the code written in functional style appear somewhat imperative.

## Monadic parser combinators

One of the tasks that easily get solved in terms of monad is parsing. One should define a set of primitive parser and then combine them to get more complex parsers. The parser code looks declarative.

## Conclusion

Java is expressive enough to define the notions of functors, monads and other functional structures and write the code in the functional style, but the code becomes too verbose.
On the bright side the code becomes easier to reason about in terms of mathematics.

## Related work

1. The idea of lightweight HKT is taken from the paper "Jeremy Yallop, Leo White - Lightweight higher-kinded polymorphism"
2. [ArrowKt](https://github.com/arrow-kt/arrow) is a general purpose functional-programming library that brings the notions of functors, monads and other category theory structures to kotlin.
3. [DataFixerUpper](https://github.com/Mojang/DataFixerUpper) is a library that is used in minecraft to define profunctor-based category theory optics (Lenses) for data migration among versions of API.
4. [avaj](https://github.com/neshkeev/avaj) Haskell influenced Category Theory patterns implemented in Java. The project defines the notion of the [Continuation](https://github.com/neshkeev/avaj/blob/master/avaj-mtl/src/main/java/com/github/neshkeev/avaj/mtl/ContTKind.java) monad, which opens the possibility to implement [coroutines](https://github.com/neshkeev/avaj/blob/master/avaj-examples/src/main/java/com/github/neshkeev/coroutines/CoroutineTKind.java) in java 8 without bytecode modifications or JNI calls.
