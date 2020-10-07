package org.example.typeclasses;

import org.example.kind.App;

public interface MonadPlus<M> extends Monad<M> {
    // mzero :: m a
    <A> App<M, A> zero();
    // mplus :: m a -> m a -> m a
    <A> App<M, A> plus(App<M, A> left, App<M, A> right);
}
