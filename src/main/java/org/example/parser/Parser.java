package org.example.parser;

import org.example.adt.Optional;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Parser<T> extends Function<String, Optional<Parser.Result<T>>> {
    class Result<T> {
        final T value;
        final String rest;

        public Result(T value, String rest) {
            this.value = value;
            this.rest = rest;
        }

        public static <T> Parser.Result<T> of(T value, String rest) {
            return new Parser.Result<>(value, rest);
        }

        public T getValue() {
            return value;
        }

        public String getRest() {
            return rest;
        }

        public <R> R caseOf(BiFunction<T, String, R> mapper) {
            return mapper.apply(value, rest);
        }

        @Override
        public String toString() {
            return "(" + value + ", " + rest + ')';
        }
    }
}
