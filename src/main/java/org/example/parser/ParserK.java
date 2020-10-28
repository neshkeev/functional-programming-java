package org.example.parser;

import org.example.adt.List;
import org.example.adt.Optional;
import org.example.kind.App;
import org.example.typeclasses.MonadPlus;

import java.util.function.BiFunction;
import java.util.function.Function;

import static org.example.adt.List.cons;
import static org.example.adt.List.nil;
import static org.example.adt.Optional.none;
import static org.example.adt.Optional.some;

public class ParserK<T> implements App<ParserK.mu, T> {
    final Parser<T> delegate;

    public ParserK(Parser<T> delegate) {
        this.delegate = delegate;
    }

    public static <T> ParserK<T> of(Parser<T> delegate) {
        return new ParserK<>(delegate);
    }

    public Parser<T> getDelegate() {
        return delegate;
    }

    public static <T> ParserK<T> narrow(App<ParserK.mu, T> value) {
        return (ParserK<T>) value;
    }

    public Optional<Parser.Result<T>> parse(String s) {
        return delegate.apply(s);
    }

    public static final class mu { }

    public enum ParserMonad implements MonadPlus<ParserK.mu> {
        INSTANCE;

        // a -> Parser<A>
        @Override
        public <A> ParserK<A> pure(A a) { return ParserK.of(s -> some(Parser.Result.of(a, s))); }

        // Parser<A> -> (A -> Parser<B>) -> Parser<B>
        @Override
        public <A, B> ParserK<B> flatMap(App<ParserK.mu, A> maK, Function<A, App<ParserK.mu, B>> aToMb) {
            final ParserK<A> narrow = narrow(maK);
            final Parser<A> sToAS = narrow.getDelegate();
            final Parser<B> sToBS = s -> sToAS.apply(s).caseOf(
                    () -> none(),
                    result -> result.caseOf(
                            (a, rest) -> aToMb
                                    .andThen(ParserK::narrow)
                                    .andThen(ParserK::getDelegate)
                                    .apply(a)
                                    .apply(rest)
                    )
            );
            return ParserK.of(sToBS);
        }

        @Override
        public <A> ParserK<A> zero() {
            return ParserK.of(__ -> none());
        }

        @Override
        public <A> ParserK<A> plus(App<ParserK.mu, A> left, App<ParserK.mu, A> right) {
            final Parser<A> leftDelegate = narrow(left).getDelegate();
            final Parser<A> rightDelegate = narrow(right).getDelegate();
            return ParserK.of(
                    s -> {
                        final Optional<Parser.Result<A>> leftResult = leftDelegate.apply(s);
                        return leftResult.caseOf(
                                () -> rightDelegate.apply(s),
                                __ -> leftResult
                        );
                    });
        }
    }

    public static ParserK<Character> anyChar() {
        return of(s -> {
            if (s.isEmpty()) return none();
            return some(Parser.Result.of(s.charAt(0), s.substring(1)));
        });
    }

    public static ParserK<Character> chr(char c) {
        return of(s -> {
            if (s.isEmpty() || s.charAt(0) != c) return none();

            return some(Parser.Result.of(s.charAt(0), s.substring(1)));
        });
    }

    public static ParserK<Character> digit() {
        final ParserK.ParserMonad m = ParserK.ParserMonad.INSTANCE;
        return m.plus(chr('0')
                , m.plus(chr('1')
                , m.plus(chr('2')
                , m.plus(chr('3')
                , m.plus(chr('4')
                , m.plus(chr('5')
                , m.plus(chr('6')
                , m.plus(chr('7')
                , m.plus(chr('8')
                , chr('9'))))))))));
    }

    public static <A> ParserK<List<A>> many(ParserK<A> parser) {
        final ParserK.ParserMonad m = ParserK.ParserMonad.INSTANCE;
        return m.plus(many1(parser), m.pure(nil()));
    }

    public static <A> ParserK<List<A>> many1(ParserK<A> parser) {
        final ParserK.ParserMonad m = ParserK.ParserMonad.INSTANCE;

        final BiFunction<A, List<A>, List<A>> cons = List::cons;
        return m.flatMap(m.pure(cons),
                appender -> m.flatMap(parser,
                nextElement -> m.flatMap(many(parser),
                rest -> m.pure(appender.apply(nextElement, rest)))
        ));
    }

    public static <A> ParserK<List<A>> manyTill(ParserK<A> parser, ParserK<A> end) {
        final ParserK.ParserMonad m = ParserK.ParserMonad.INSTANCE;

        final ParserK<List<A>> stop = m.flatMap(end, __ -> m.pure(nil()));

        final ParserK<List<A>> proceed = m.flatMap(parser,
                nextElement -> m.flatMap(manyTill(parser, end),
                rest -> m.pure(cons(nextElement, rest))
        ));

        return m.plus(stop, proceed);
    }

    public static <T> ParserK<Optional<T>> opt(ParserK<T> parser) {
        final ParserK.ParserMonad m = ParserK.ParserMonad.INSTANCE;
        final ParserK<Optional<T>> success = m.flatMap(parser, p -> m.pure(some(p)));
        return m.plus(success, m.pure(none()));
    }
}
