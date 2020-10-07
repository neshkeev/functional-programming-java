package org.example.step3;

import org.example.kind.*;
import org.example.step3.functors.*;

import java.util.function.Function;

import static org.example.adt.List.*;

public class Functors {
    public static void main(String[] args) {
        final Function<String, String> worldAppender = hello -> hello + ", World";

        // ID Functor
        final IdFunctor idFunctor = IdFunctor.INSTANCE;
        final IdK<String> idHello = idFunctor.map(worldAppender, IdK.of("Hello"));
        System.out.println(idHello);

        // Optional Functor
        final OptionalFunctor optionalFunctor = OptionalFunctor.INSTANCE;
        final OptionalK<String> helloOptional = OptionalK.of("Hello");

        final OptionalK<String> appSome = optionalFunctor.map(worldAppender, helloOptional);
        final OptionalK<String> appNone = optionalFunctor.map(worldAppender, OptionalK.none());

        System.out.println(appSome);
        System.out.println(appNone);

        // List Functor
        final ListFunctor listFunctor = ListFunctor.INSTANCE;

        final ListK<String> list = ListK.of(cons("Hello", cons("Goodbye", nil())));
        final ListK<String> consMessages = listFunctor.map(worldAppender, list);
        final ListK<String> nilMessages = listFunctor.map(worldAppender, ListK.of(nil()));

        System.out.println(consMessages);
        System.out.println(nilMessages);
    }
}
