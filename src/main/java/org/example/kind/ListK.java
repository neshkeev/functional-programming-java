package org.example.kind;

import org.example.adt.List;
import org.example.typeclasses.Functor;
import org.example.typeclasses.Monad;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static org.example.adt.List.cons;
import static org.example.adt.List.nil;

public class ListK<T> implements App<ListK.mu, T> {
    private final List<T> delegate;

    public ListK(List<T> delegate) { this.delegate = delegate; }
    public ListK(T head, List<T> tail) { this.delegate = cons(head, tail); }

    public static <T> ListK<T> of(List<T> delegate) { return new ListK<>(delegate); }
    public static <T> ListK<T> of(T head, List<T> delegate) { return new ListK<>(head, delegate); }

    public static<T> ListK<T> narrow(App<mu, T> value) { return (ListK<T>) value; }

    public List<T> getDelegate() { return delegate; }

    public static final class mu { }

    public enum ListMonad implements Monad<mu> {
        INSTANCE;

        @Override
        public <A> ListK<A> pure(A a) {
            return of(a, nil());
        }

        @Override
        public <A, B> ListK<B> flatMap(App<mu, A> maK, Function<A, App<mu, B>> aToMb) {
            final List<A> ma = narrow(maK).getDelegate();
            return ma.caseOf(
                    () -> of(nil()),
                    (head, tail) -> {
                        final List<B> newHead = narrow(aToMb.apply(head)).getDelegate();
                        final List<B> newTail = narrow(flatMap(of(tail), aToMb)).getDelegate();
                        return of(newHead.concat(newTail));
                    }
            );
        }

        @Override
        public <A, B> ListK<B> map(Function<A, B> fun, App<mu, A> ma) {
            return narrow(Monad.super.map(fun, ma));
        }

    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}