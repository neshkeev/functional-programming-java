package org.example.step3.functors;

import org.example.kind.App;
import org.example.kind.IdK;
import org.example.typeclasses.Functor;

import java.util.function.Function;

public enum IdFunctor implements Functor<IdK.mu> {
    INSTANCE;

    @Override
    public <A, B> IdK<B> map(Function<A, B> fun, App<IdK.mu, A> value) {
        final A a = IdK.narrow(value).getValue();
        return fun.andThen(IdK::new).apply(a);
    }

}
