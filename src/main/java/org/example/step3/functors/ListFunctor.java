package org.example.step3.functors;

import org.example.adt.List;
import org.example.kind.App;
import org.example.kind.ListK;
import org.example.typeclasses.Functor;

import java.util.function.Function;

import static org.example.adt.List.nil;

public enum ListFunctor implements Functor<ListK.mu> {
    INSTANCE;

    @Override
    public <A, B> ListK<B> map(Function<A, B> fun, App<ListK.mu, A> value) {
        final List<A> delegate = ListK.narrow(value).getDelegate();
        return delegate.caseOf(
                () -> ListK.of(nil()),
                (head, tail) -> {
                    final App<ListK.mu, B> app = map(fun, ListK.of(tail));

                    final B newHead = fun.apply(head);
                    final List<B> newTail = ListK.narrow(app).getDelegate();
                    return ListK.of(newHead, newTail);
                }
        );
    }
}
