package org.example.steps.step5;

import org.example.kind.App;
import org.example.kind.IdK;
import org.example.kind.IdK.IdMonad;
import org.example.kind.ListK;
import org.example.kind.ListK.ListMonad;
import org.example.kind.OptionalK;
import org.example.kind.OptionalK.OptionalMonad;
import org.example.typeclasses.Monad;

import static org.example.adt.List.nil;

public class Monads {
    public static void main(String[] args) {
        final IdMonad idMonad = IdMonad.INSTANCE;
        final OptionalMonad optMonad = OptionalMonad.INSTANCE;
        final ListMonad listMonad = ListMonad.INSTANCE;

        final IdK<String> hwId = idMonad.flatMap(idMonad.pure("Hello"),
                hello -> idMonad.flatMap(idMonad.pure("World"),
                    world -> idMonad.pure(hello + ", " + world + "!")
                ));
        System.out.println(hwId);

        final OptionalK<String> hwOpt = optMonad.flatMap(optMonad.pure("Hello"),
                hello -> optMonad.flatMap(optMonad.pure("World"),
                world -> optMonad.pure(hello + ", " + world + "!")
        ));
        System.out.println(hwOpt);

        System.out.println(getGreet(idMonad));
        System.out.println(getGreet(optMonad));
        System.out.println(getGreet(listMonad));

        System.out.println(getGreet(idMonad, IdK.of("Goodbye")));
        System.out.println(getGreet(optMonad, OptionalK.some("IGNORE")));
        System.out.println(getGreet(listMonad, ListK.of("Goodbye", nil())));
    }

    private static <M> App<M, String> getGreet(Monad<M> m) {
        return m.flatMap(m.pure("Hello"),
                hello -> m.flatMap(m.pure("World"),
                world -> m.pure(hello + ", " + world + "!")
        ));
    }

    private static <M> App<M, String> getGreet(Monad<M> m, App<M, String> startValue) {
        return m.flatMap(startValue,
                hello -> {
                    if (hello.equals("IGNORE")) return m.pure("Ignoring the rest of the monadic computation");
                    return m.flatMap(m.pure("World"),
                            world -> m.pure(hello + ", " + world + "!")
                            );
                });
    }
}
