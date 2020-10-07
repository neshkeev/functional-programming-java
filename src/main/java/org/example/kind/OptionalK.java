package org.example.kind;

import org.example.adt.Optional;
import org.example.typeclasses.Functor;
import org.example.typeclasses.Monad;

import java.util.function.Function;

import static org.example.adt.Optional.some;

public class OptionalK<T> implements App<OptionalK.mu, T> {
    final Optional<T> delegate;

    public OptionalK(Optional<T> delegate) {
        this.delegate = delegate;
    }

    public OptionalK(T value) {
        this.delegate = some(value);
    }

    public static <T> OptionalK<T> of(T value) {
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

    public enum OptionalMonad implements Monad<mu> {
        INSTANCE;

        @Override
        public <A> OptionalK<A> pure(A a) {
            return of(a);
        }

        @Override
        public <A, B> OptionalK<B> flatMap(App<mu, A> maK, Function<A, App<mu, B>> aToMb) {
            final Optional<A> delegate = narrow(maK).getDelegate();
            final App<mu, B> result = delegate.caseOf(
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
