package org.example.steps.step4.functors;

import org.example.adt.Optional;
import org.example.kind.App;
import org.example.kind.OptionalK;
import org.example.typeclasses.Functor;

import java.util.function.Function;

public enum OptionalFunctor implements Functor<OptionalK.mu> {
    INSTANCE;

    @Override
    public <A, B> OptionalK<B> map(Function<A, B> fun, App<OptionalK.mu, A> value) {
        final Optional<A> delegate = OptionalK.narrow(value).getDelegate();
        return delegate.caseOf(
                OptionalK::none,
                val -> fun.andThen(Optional::some)
                        .andThen(OptionalK::new)
                        .apply(val)
        );
    }
}
