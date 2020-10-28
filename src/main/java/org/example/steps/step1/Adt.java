package org.example.steps.step1;

import org.example.adt.Id;
import org.example.adt.List;
import org.example.adt.Optional;
import org.example.adt.Tree;

import static org.example.adt.List.cons;
import static org.example.adt.List.nil;
import static org.example.adt.Optional.none;
import static org.example.adt.Optional.some;
import static org.example.adt.Tree.leaf;
import static org.example.adt.Tree.node;

public class Adt {
    public static void main(String[] args) {
        // Id
        System.out.println("Id:");
        System.out.println(Id.of("Hello"));

        // Optional
        System.out.println("=======================");
        System.out.println("Optional:");
        final Optional<String> optionalHello = some("Hello");
        final Optional<String> none = none();

        System.out.println(optionalHello + " is empty? " + optionalHello.isNone());
        System.out.println(none + " is empty? " + none.isNone());

        // List
        System.out.println("=======================");
        System.out.println("List:");
        final List<String> cons = cons("Hello", cons("Goodbye", nil()));
        final List<String> nil = nil();
        System.out.println(get(cons, 1));
        System.out.println(get(nil, 2));

        // Tree
        System.out.println("=======================");
        System.out.println("Tree:");
        System.out.println("   Hello      ");
        System.out.println("   /   \\      ");
        System.out.println("  ()  Goodbye ");
        System.out.println("      /     \\ ");
        System.out.println("     ()     ()");
        final Tree<String> tree = node(leaf(), "Hello", node(leaf(), "Goodbye", leaf()));
        System.out.println(tree);
    }

    private static <T> Optional<T> get(List<T> list, long i) {
        if (list instanceof List.Nil) return none();
        final List.Cons<T> cons = (List.Cons<T>) list;
        if (i == 0) {
            return some(cons.getHead());
        }
        return get(cons.getTail(), i - 1);
    }
}
