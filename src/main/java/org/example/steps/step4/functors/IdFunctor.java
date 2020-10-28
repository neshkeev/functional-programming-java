package org.example.steps.step4.functors;

import org.example.kind.App;
import org.example.kind.IdK;
import org.example.typeclasses.Functor;

import java.util.function.Function;

public enum IdFunctor implements Functor<IdK.mu> {
    INSTANCE;

    @Override
    public <A, B> IdK<B> map(Function<A, B> fun, App<IdK.mu, A> value) {
        final IdK<A> narrow = IdK.narrow(value);
        final A a = narrow.getValue();
        return fun.andThen(IdK::new).apply(a);
    }

}
