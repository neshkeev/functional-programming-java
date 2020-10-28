package org.example.steps.step4;

import org.example.kind.*;
import org.example.steps.step4.functors.*;
import org.example.typeclasses.Functor;

import java.util.function.Function;

import static org.example.adt.List.*;
import static org.example.kind.OptionalK.none;
import static org.example.kind.OptionalK.some;

public class Functors {

    private static String appendWorld(String hello) {
        return hello + ", World!";
    }

    private static <F, I, O> App<F, O> map(
            Functor<F> functor,
            Function<I, O> mapper,
            App<F, I> value
    ) {
        return functor.map(mapper, value);
    }

    public static void main(String[] args) {
        // ID Functor
        final IdK<String> originalId = IdK.of("Hello");
        System.out.println(map(IdFunctor.INSTANCE, Functors::appendWorld, originalId));

        // Optional Functor
        final OptionalFunctor optionalFunctor = OptionalFunctor.INSTANCE;

        final OptionalK<String> originalOptional = some("Hello");
        System.out.println(map(optionalFunctor, Functors::appendWorld, originalOptional));
        System.out.println(map(optionalFunctor, Functors::appendWorld, none()));

        // List Functor
        final ListFunctor listFunctor = ListFunctor.INSTANCE;

        final ListK<String> originalList = ListK.of(cons("Hello", cons("Goodbye", nil())));

        System.out.println(map(listFunctor, Functors::appendWorld, originalList));
        System.out.println(map(listFunctor, Functors::appendWorld, ListK.of(nil())));
    }
}
