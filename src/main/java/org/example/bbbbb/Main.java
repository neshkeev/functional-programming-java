package org.example.bbbbb;

import java.util.function.Function;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        System.out.println(Optional.some("Hello").map(String::length));
        System.out.println(Optional.<String>none().map(String::length));
    }

    public static abstract class Optional<T> {
        private Optional() {}
        public static <T> Some<T> some(T value) { return new Some<>(value); }
        public static <T> None<T> none() { return new None<>(); }

        public <R> R caseOf(
                Supplier<R> noneOp,
                Function<T, R> someOp
        ) {
            if (this instanceof None) return noneOp.get();
            Some<T> some = (Some<T>) this;
            return someOp.apply(some.getValue());
        }

        public <R> Optional<R> map(Function<T, R> mapper) {
            return caseOf(
                    () -> none(),
                    value -> some(mapper.apply(value))
            );
        }

        public static final class None<T> extends Optional<T> {
            @Override
            public String toString() {
                return "None";
            }
        }
        public static final class Some<T> extends Optional<T> {
            final T value;
            public Some(T value) { this.value = value; }

            public T getValue() { return value; }

            @Override
            public String toString() {
                return "Some(" + value + ')';
            }
        }
    }
}
