package org.example.adt;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Optional<T> {
    private Optional() { }

    public static <T> Some<T> some(T value) {
        return new Some<>(value);
    }

    public static <T> None<T> none() {
        return new None<>();
    }

    public static final class None<T> extends Optional<T> {
        @Override
        public String toString() {
            return "None";
        }
    }

    public static final class Some<T> extends Optional<T> {
        final T value;

        public Some(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Some(" + value + ')';
        }
    }

    public <R> R caseOf(
            Supplier<R> noneOp,
            Function<T, R> someOp
    ) {
        if (this instanceof None) {
            return noneOp.get();
        }
        return someOp.apply(((Some<T>) this).getValue());
    }

    public boolean isNone() {
        return caseOf(
                () -> true,
                __ -> false
        );
    }
}
