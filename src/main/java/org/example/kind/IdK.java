package org.example.kind;

import org.example.typeclasses.Monad;

import java.util.function.Function;

// F<A> -> App<F, A>
public class IdK<T> implements App<IdK.mu, T> {
    final T value;

    public IdK(T value) {
        this.value = value;
    }

    public static <T> IdK<T> of(T value) {
        return new IdK<>(value);
    }

    public static final class mu { }

    // App<F, A> -> F<A>
    public static <T> IdK<T> narrow(App<mu, T> value) {
        return (IdK<T>) value;
    }

    public T getValue() {
        return value;
    }

    public enum IdMonad implements Monad<mu> {
        INSTANCE;

        @Override
        public <A> IdK<A> pure(A a) {
            return IdK.of(a);
        }

        @Override
        public <A, B> IdK<B> flatMap(App<mu, A> ma, Function<A, App<mu, B>> aToMb) {
            final App<mu, B> result = aToMb.apply(narrow(ma).getValue());
            return narrow(result);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
