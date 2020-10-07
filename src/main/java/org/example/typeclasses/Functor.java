package org.example.typeclasses;

import org.example.kind.App;

import java.util.function.Function;

public interface Functor<F> {
    // (a -> b) -> f a -> f b
    <A, B> App<F, B> map(Function<A, B> fun, App<F, A> value);
}
