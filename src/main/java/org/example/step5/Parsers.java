package org.example.step5;

import org.example.adt.List;
import org.example.parser.Parser;
import org.example.parser.ParserK;
import org.example.parser.ParserK.ParserMonad;

import java.util.function.Function;

class ThreeChars {
    final Character a, b, c;

    ThreeChars(Character a, Character b, Character c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    // char -> char -> char -> ThreeChars
    static Function<Character, Function<Character, Function<Character, ThreeChars>>> curriedCtor () {
        return a -> b -> c -> new ThreeChars(a, b, c);
    }

    @Override
    public String toString() {
        return "(" + a + "," + b + "," + c + ")";
    }
}

public class Parsers {
    public static void main(String[] args) {
        // any char parser
        System.out.println(ParserK.anyChar().getDelegate().apply("ABC"));
        System.out.println(ParserK.anyChar().getDelegate().apply(""));

        // monadic
        final ParserK<Character> anyChar = ParserK.anyChar();
        final ParserMonad m = ParserMonad.INSTANCE;
        final Parser<ThreeChars> t = m.flatMap(m.pure(ThreeChars.curriedCtor()),
                f -> m.flatMap(anyChar,
                a -> m.flatMap(anyChar,
                b -> m.flatMap(anyChar,
                c -> m.pure(f.apply(a).apply(b).apply(c))
        )))).getDelegate();

        System.out.println(t.apply("A"));
        System.out.println(t.apply("AB"));
        System.out.println(t.apply("ABC"));
        System.out.println(t.apply("ABCD"));

        final ParserK<Character> parseA = ParserK.chr('A');
        final ParserK<Character> parseB = ParserK.chr('B');
        final Parser<Character> aOrB = m.plus(parseA, parseB).getDelegate();
        System.out.println(aOrB.apply("ABC"));
        System.out.println(aOrB.apply("BCD"));
        System.out.println(aOrB.apply("CDE"));
        System.out.println(aOrB.apply(""));

        final Parser<List<Character>> many1A = ParserK.many(ParserK.chr('A')).getDelegate();
        System.out.println(many1A.apply("BB"));
        System.out.println(many1A.apply("A"));
        System.out.println(many1A.apply("ABB"));
        System.out.println(many1A.apply("AABB"));
        System.out.println(many1A.apply("AABBA"));
    }
}
