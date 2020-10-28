package org.example.steps.step7;

import org.example.adt.List;
import org.example.adt.Optional;
import org.example.parser.ParserK;

import java.util.function.Supplier;

import static org.example.adt.List.cons;
import static org.example.adt.List.nil;
import static org.example.parser.ParserK.*;

class JsonParser {
    public static abstract class Value {
        private Value() {}
        public static final class String extends Value {
            final java.lang.String value;
            protected String(java.lang.String value) { this.value = value; }

            @Override
            public java.lang.String toString() {
                return value;
            }
        }
        public static final class Number extends Value {
            final java.lang.Number value;
            protected Number(java.lang.Number value) { this.value = value; }

            @Override
            public java.lang.String toString() {
                return value.toString();
            }
        }
        public static final class Object<T> extends Value {
            final T value;
            protected Object(T value) { this.value = value; }

            @Override
            public java.lang.String toString() {
                return value.toString();
            }
        }
        public static final class Array<T> extends Value {
            final List<T> value;
            public Array(List<T> value) { this.value = value; }

            @Override
            public java.lang.String toString() {
                return value.toString();
            }
        }
        public static final class Null extends Value {
            @Override
            public java.lang.String toString() {
                return "null";
            }
        }
        public static final class Bool extends Value {
            final boolean value;
            public Bool(boolean value) { this.value = value; }

            @Override
            public java.lang.String toString() {
                return Boolean.toString(value);
            }
        }
    }

    public static ParserK<List<Character>> whitespace() {
        final ParserK.ParserMonad m = ParserK.ParserMonad.INSTANCE;
        return many(m.plus(
                chr(' '), m.plus(
                chr('\n'),
                chr('\t')
                )));
    }
    public static ParserK<Value.Number> number() {
        final ParserK.ParserMonad m = ParserK.ParserMonad.INSTANCE;
        final ParserK<String> integral = m.flatMap(
                opt(chr('-')),
                neg -> m.flatMap(
                many1(digit()),
                digits -> {
                    final String num = digits.string();
                    return neg.caseOf(
                            () -> m.pure(num),
                            __ -> m.pure("-" + num)
                    );
                }
        ));

        final ParserK<Optional<String>> decimal = opt(m.flatMap(
                chr('.'),
                comma -> m.flatMap(
                many1(digit()),
                digits -> m.pure(digits.string())
        )));

        return m.flatMap(
                integral, num -> m.flatMap(
                decimal, dec -> dec.caseOf(
                        () -> m.pure(new Value.Number(Long.parseLong(num))),
                        d -> m.pure(new Value.Number(Double.parseDouble(num + d)))
                )));
    }
    public static ParserK<Value.String> string() {
        final ParserK.ParserMonad m = ParserK.ParserMonad.INSTANCE;
        return m.flatMap(
                chr('"'), __ -> m.flatMap(
                manyTill(anyChar(), chr('"')),
                val -> m.pure(new Value.String(val.string()))
        ));
    }
    public static ParserK<Value.Null> nullValue() {
        final ParserK.ParserMonad m = ParserK.ParserMonad.INSTANCE;
        return m.flatMap(
                chr('n'), _n -> m.flatMap(
                chr('u'), _u -> m.flatMap(
                chr('l'), _l -> m.flatMap(
                chr('l'), __ -> m.pure(new Value.Null())
        ))));
    }
    public static ParserK<Value.Bool> fls() {
        final ParserK.ParserMonad m = ParserK.ParserMonad.INSTANCE;
        return m.flatMap(
            chr('f'), _f -> m.flatMap(
            chr('a'), _a -> m.flatMap(
            chr('l'), _l -> m.flatMap(
            chr('s'), _s -> m.flatMap(
            chr('e'), __ -> m.pure(new Value.Bool(false))
        )))));
    }
    public static ParserK<Value.Bool> tru() {
        final ParserK.ParserMonad m = ParserK.ParserMonad.INSTANCE;
        return m.flatMap(
            chr('t'), _t -> m.flatMap(
            chr('r'), _r -> m.flatMap(
            chr('u'), _u -> m.flatMap(
            chr('e'), __ -> m.pure(new Value.Bool(true))
        ))));
    }

    public static class Record {
        final String name;
        final Value value;

        Record(String name, Value value) {
            this.name = name;
            this.value = value;
        }
        @Override
        public String toString() {
            return "{" + name + " = " + value + '}';
        }
    }

    public static ParserK<Value.Object<List<Record>>> object() {
        final ParserK.ParserMonad m = ParserK.ParserMonad.INSTANCE;

        // "name" : value
        final Supplier<ParserK<Record>> attr = () -> m.flatMap(
                whitespace(), _w1 -> m.flatMap(
                string(), name -> m.flatMap(
                whitespace(), _w2 -> m.flatMap(
                chr(':'), _colon -> m.flatMap(
                whitespace(), _w3 -> m.flatMap(
                value(), value -> m.pure(new Record(name.value, value))
        ))))));

        // "name" : "value", "name" : "value", "name2": "value2"
        final Supplier<ParserK<List<Record>>> attrs = () -> m.flatMap(
                attr.get(), record -> m.flatMap(
                many(m.flatMap(
                        chr(','), __ ->
                        attr.get()
                )), records ->
                m.pure(cons(record, records))
        ));

        return m.flatMap(
                chr('{'), _l -> m.flatMap(
                whitespace(), _ws1 -> m.flatMap(
                opt(attrs.get()), elements -> m.flatMap(
                whitespace(), _ws2 -> m.flatMap(
                chr('}'), _r ->
                m.pure(elements.caseOf(
                        () -> new Value.Object<>(nil()),
                        elems -> new Value.Object<>(elems)
                ))
        )))));
    }
    public static ParserK<Value.Array<Value>> array() {
        final ParserK.ParserMonad m = ParserK.ParserMonad.INSTANCE;

        final Supplier<ParserK<List<Value>>> elements = () -> m.flatMap(
                value(), value -> m.flatMap(
                many(m.flatMap(
                        whitespace(), _ws2 -> m.flatMap(
                        chr(','), __ -> m.flatMap(
                        whitespace(), _ws3 ->
                        value()
                )))),
                values -> m.pure(cons(value, values))
        ));

        return m.flatMap(
                chr('['), _l -> m.flatMap(
                whitespace(), _ws1 -> m.flatMap(
                opt(elements.get()), res -> m.flatMap(
                whitespace(), _ws2 -> m.flatMap(
                chr(']'), _r -> m.pure(
                        res.caseOf(
                                () -> new Value.Array<>(nil()),
                                elems -> new Value.Array<>(elems))
                        )
        )))));
    }
    public static ParserK<Value> value() {
        final ParserK.ParserMonad m = ParserK.ParserMonad.INSTANCE;
        return m.plus(
            m.map(e -> e, string()), m.plus(
            m.map(e -> e, number()), m.plus(
            m.map(e -> e, object()), m.plus(
            m.map(e -> e, array()), m.plus(
            m.map(e -> e, nullValue()), m.plus(
            m.map(e -> e, tru()),
            m.map(e -> e, fls())
        ))))));
    }
}

public class RealWorldParsers {
    public static void main(String[] args) {
//        System.out.println(JsonParser.whitespace().parse(" \n \t\t\n d"));
//        System.out.println(JsonParser.whitespace().parse(""));
//        System.out.println(JsonParser.number().parse("-123"));
//        System.out.println(JsonParser.number().parse("-1.2"));
//        System.out.println(JsonParser.number().parse("-0.2"));
//        System.out.println(JsonParser.number().parse("0"));
//        System.out.println(JsonParser.number().parse("-0"));
//        System.out.println(JsonParser.string().parse("\"abc\" "));
//        System.out.println(JsonParser.object().parse("{   \t \n   \t\t\t\n}"));
//        System.out.println(JsonParser.array().parse("[ \"hello\",\"world\"]"));
//        System.out.println(JsonParser.value().parse("[ \t\n     \"hello\"\t      ,    \"world\"    ]"));

        System.out.println(JsonParser.value().parse("{" +
                "\"greet\": \"hello\"," +
                "\"what\": [\"world\", \"universe\"]," +
                "\"now\": true" +
                "}"));
        System.out.println(JsonParser.value().parse(""));
    }
}
