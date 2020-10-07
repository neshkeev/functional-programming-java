package org.example.step4;

import org.example.kind.App;
import org.example.kind.IdK;
import org.example.kind.IdK.IdMonad;
import org.example.kind.ListK;
import org.example.kind.OptionalK;
import org.example.kind.OptionalK.OptionalMonad;
import org.example.typeclasses.Monad;

import static org.example.adt.List.nil;

public class Monads {
    public static void main(String[] args) {
        final IdMonad idMonad = IdMonad.INSTANCE;
        final OptionalMonad optMonad = OptionalMonad.INSTANCE;
        final ListK.ListMonad listMonad = ListK.ListMonad.INSTANCE;

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

        System.out.println(getMessage(idMonad));
        System.out.println(getMessage(optMonad));
        System.out.println(getMessage(listMonad));

        System.out.println(getMessage(idMonad, IdK.of("Goodbye")));
        System.out.println(getMessage(optMonad, OptionalK.of("IGNORE")));
        System.out.println(getMessage(listMonad, ListK.of("Goodbye", nil())));
    }

    private static <M> App<M, String> getMessage(Monad<M> m) {
        return m.flatMap(m.pure("Hello"),
                hello -> m.flatMap(m.pure("World"),
                world -> m.pure(hello + ", " + world + "!")
        ));
    }

    private static <M> App<M, String> getMessage(Monad<M> m, App<M, String> startValue) {
        return m.flatMap(startValue,
                hello -> {
                    if (hello.equals("IGNORE")) return m.pure("Ignoring the rest of the monadic computation");
                    return m.flatMap(m.pure("World"),
                            world -> m.pure(hello + ", " + world + "!")
                    );
                });
    }
}
