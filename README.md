# Functional programming in Java

## Contents

0. [Closure](src/main/java/org/example/step0/Closure.java) is a reminder for the idea of closures, which is a corner stone for monadic computations
1. [ADT and Pattern Matching](src/main/java/org/example/step1/AdtAndPatternMatching.java) demonstrates how to define Algebraic Data Types (ADT) and pattern matching over them
2. [Mapper over ADTs](src/main/java/org/example/step2/MapOverContainers.java) demonstrates a naive approach to define a set of mappers over ADTs
3. [HKT and Functors](src/main/java/org/example/step3/Functors.java) introduces the notion of Lightweight Higher-Kinded polymorphism (aka. Higher-Kinded Types, HKT) and defines the notion of a functor
4. [Monads](src/main/java/org/example/step4/Monads.java) demonstrates the idea of monads
5. [Monadic parser combinators](src/main/java/org/example/step5/Parsers.java) defines a set of monadic parser combinators
6. [Real world parser combinators example](src/main/java/org/example/step6/RealWorldParsers.java) defines a monadic JSON parser that is built from the monadic parser combinators

## Related work

1. The idea of lightweight HKT is taken from the paper "Jeremy Yallop, Leo White - Lightweight higher-kinded polymorphism"
2. [ArrowKt](https://github.com/arrow-kt/arrow) brings the purely functional constructions like functors, monads, applicative, etc. to Kotlin by following the ideas from the article above. Written in Kotlin
3. [DataFixerUpper](https://github.com/Mojang/DataFixerUpper) defines the notion of Profunctors and Lenses for the means of data transformation by following the ideas from the article above. Written in Java
4. [avaj](https://github.com/neshkeev/avaj) Haskell influenced Category Theory patterns implemented in Java. The project defines the notion of the [Continuation](https://github.com/neshkeev/avaj/blob/master/avaj-mtl/src/main/java/com/github/neshkeev/avaj/mtl/ContTKind.java) monad, which opens the possibility to implement [coroutines](https://github.com/neshkeev/avaj/blob/master/avaj-examples/src/main/java/com/github/neshkeev/coroutines/CoroutineTKind.java) in java 8 without bytecode modifications or JNI calls.
