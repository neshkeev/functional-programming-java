package org.example.step1;

import org.example.adt.*;

import java.util.function.Function;

import static org.example.adt.Optional.*;
import static org.example.adt.List.*;
import static org.example.adt.Tree.*;

public class AdtAndPatternMatching {
    public static void main(String[] args) {
        // Id
        System.out.println("Id:");
        final Id<String> idHello = Id.of("Hello");
        System.out.println(idHello);
        System.out.println("Content of " + idHello + " is of size " + idHello.caseOf(String::length));
        
        // Optional
        System.out.println("=======================");
        System.out.println("Optional:");
        final Some<String> optionalHello = some("Hello");
        final None<String> none = none();

        System.out.println(optionalHello + " is empty? " + optionalHello.isNone());
        System.out.println(none + " is empty? " + none.isNone());
        final int sizeSome = optionalHello.caseOf(
                () -> 0,
                s -> s.length()
        );
        final int sizeNone = none.caseOf(
                () -> 0,
                s -> s.length()
        );
        System.out.println("Content of " + optionalHello + " is of size " + sizeSome);
        System.out.println("Content of " + none + " is of size " + sizeNone);

        // List
        System.out.println("=======================");
        System.out.println("List:");
        final List<String> list = cons("Hello", cons("Goodbye", nil()));
        final List<String> nil = nil();
        final Optional<String> headElement = getHead(list);
        final Optional<String> headElementOfNil = getHead(nil);

        System.out.println("Head of " + list + " is " + headElement);
        System.out.println("Head of " + nil + " is " + headElementOfNil);

        System.out.println(map(e -> e + ", World!", list));
        System.out.println(map(e -> e + ", World!", nil));

        // Tree
        System.out.println("=======================");
        System.out.println("Tree:");
        System.out.println("   Hello      ");
        System.out.println("   /   \\      ");
        System.out.println("  ()  Goodbye ");
        System.out.println("      /     \\ ");
        System.out.println("     ()     ()");
        final Tree<String> tree = node(leaf(), "Hello", node(leaf(), "Goodbye", leaf()));
        final Tree<String> messagesTree = map(e -> e + ", World!", tree);
        final Tree<String> messagesLeaf = map(e -> e + ", World!", leaf());
        System.out.println("'" + tree + "' is transformed to '" + messagesTree + "'");
        System.out.println("'" + leaf() + "' is transformed to '" + messagesLeaf + "'");
    }

    private static Optional<String> getHead(List<String> nil) {
        return nil.caseOf(
                () -> none(),
                (head, __) -> some(head)
        );
    }

    private static <I, O> List<O> map(Function<I, O> mapper, List<I> in) {
        return in.caseOf(
                () -> nil(),
                (head, tail) -> cons(mapper.apply(head), map(mapper, tail))
        );
    }

    private static <I, O> Tree<O> map(Function<I, O> mapper, Tree<I> in) {
        return in.caseOf(
                () -> Tree.leaf(),
                left -> value -> right -> node(map(mapper, left), mapper.apply(value), map(mapper, right))
        );
    }
}


























