package org.example.step2;

import org.example.adt.Id;
import org.example.adt.List;
import org.example.adt.Optional;
import org.example.adt.Tree;

import java.util.function.Function;

import static org.example.adt.List.*;
import static org.example.adt.Tree.*;
import static org.example.adt.Optional.*;

public class MapOverContainers {
    public static void main(String[] args) {
        final Function<String, String> worldAppender = hello -> hello + ", World";
        final Id<String> hwId = MapperUtil.map(worldAppender, Id.of("Hello"));
        System.out.println(hwId);

        final Optional<String> hwSome = MapperUtil.map(worldAppender, some("Hello"));
        final Optional<String> none = MapperUtil.map(worldAppender, none());
        System.out.println(hwSome);
        System.out.println(none);

        final List<String> messagesList = MapperUtil.map(worldAppender, cons("Hello", cons("Goodbye", nil())));
        final List<String> nil = MapperUtil.map(worldAppender, nil());
        System.out.println(messagesList);

        final Tree<String> messagesTree = MapperUtil.map(worldAppender, node(leaf(), "Hello", node(leaf(), "Goodbye", leaf())));
        final Tree<String> leaf = MapperUtil.map(worldAppender, leaf());
        System.out.println(messagesList);
        System.out.println(leaf);
    }
}
