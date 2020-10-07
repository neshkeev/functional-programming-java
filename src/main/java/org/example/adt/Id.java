package org.example.adt;

import java.util.function.Function;

// ADT
public class Id<T> {
    final T value;

    public Id(T value) {
        this.value = value;
    }

    public static <T> Id<T> of(T value) {
        return new Id<>(value);
    }

    public T getValue() {
        return value;
    }

    public <R> R caseOf(Function<T, R> mapper) {
        return mapper.apply(value);
    }

    @Override
    public String toString() {
        return "Id(" + value + ')';
    }
}
