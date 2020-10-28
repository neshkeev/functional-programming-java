package org.example.typeclasses;

import org.example.kind.App;

import java.util.function.Function;

public interface Monad<M> extends Functor<M> {

//    @Contract(pure = true)
    // a -> m a
    <A> App<M, A> pure(final A a);

    // m a -> (a -> m b) -> m b
    <A, B> App<M, B> flatMap(
            final App<M, A> ma,
            final Function<A, App<M, B>> aToMb
    );

    @Override
    default <A, B> App<M, B> map(Function<A, B> fun, App<M, A> ma) {
        return this.flatMap(ma, a -> this.pure(fun.apply(a)));
    }
}
