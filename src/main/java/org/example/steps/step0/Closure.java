package org.example.steps.step0;

import java.util.Optional;
import java.util.function.Function;

// closure
public class Closure {
    public static void main(String[] args) {
        // flatMap :: Optional<String> -> (String -> Optional<String>) -> Optional<String>
        Optional<String> helloWorld = Optional.of("Hello")
                .flatMap(hello -> Optional.of("World").flatMap(
                        world -> Optional.of(hello + ", " + world + "!")
                    )
                );

        System.out.println(helloWorld.get());

        // String -> Integer
        Function<String, Integer> length = s -> s.length();
        // Integer -> Boolean
        Function<Integer, Boolean> isEven = n -> n % 2 == 0;
        // String -> Boolean
        Function<String, Boolean> evenStringLengths = s -> length.andThen(isEven).apply(s);
        // String -> Boolean
        Function<String, Boolean> evenStringLengthsEta = length.andThen(isEven);

        System.out.println(evenStringLengths.apply("Hello"));
        System.out.println(evenStringLengths.apply("Hi"));

        System.out.println(evenStringLengthsEta.apply("Hello"));
        System.out.println(evenStringLengthsEta.apply("Hi"));
    }
}
