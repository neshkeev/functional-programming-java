package org.example.step0;

import java.util.Optional;

// closure
public class Closure {
    public static void main(String[] args) {
        // flatMap :: Optional<String> -> (String -> Optional<String>) -> Optional<String>
        final Optional<String> helloWorld = Optional.of("Hello")
                .flatMap(hello -> Optional.of("World").flatMap(
                        world -> Optional.of(hello + ", " + world + "!")
                ));

        System.out.println(helloWorld.get());
    }
}
