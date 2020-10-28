package org.example.kind;

import org.example.adt.Optional;
import org.example.typeclasses.Monad;

import java.util.function.Function;

public class OptionalK<T> implements App<OptionalK.mu, T> {
    final Optional<T> delegate;

    public OptionalK(Optional<T> delegate) {
        this.delegate = delegate;
    }

    public OptionalK(T value) {
        this.delegate = Optional.some(value);
    }

    public static <T> OptionalK<T> some(T value) {
        return new OptionalK<>(value);
    }

    public static <T> OptionalK<T> none() {
        return new OptionalK<>(Optional.none());
    }

    public static final class mu { }

    public static <T> OptionalK<T> narrow(App<mu, T> value) {
        return (OptionalK<T>) value;
    }

    public Optional<T> getDelegate() {
        return delegate;
    }

    public enum OptionalMonad implements Monad<OptionalK.mu> {
        INSTANCE;

        // A -> Optional<A>
        @Override
        public <A> OptionalK<A> pure(A a) {
            return some(a);
        }

        // OptionalK<A> -> (A -> OptionalK<B>) -> OptionalK<B>
        @Override
        public <A, B> OptionalK<B> flatMap(App<OptionalK.mu, A> maK, Function<A, App<OptionalK.mu, B>> aToMb) {
            final Optional<A> delegate = narrow(maK).getDelegate();
            final App<OptionalK.mu, B> result = delegate.caseOf(
                    () -> none(),
                    value -> aToMb.apply(value)
            );
            return narrow(result);
        }
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
