package org.example.steps.step2;

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
import static org.example.steps.step2.MapperUtil.map;

public class PatternMatching {

    // String -> String
    private static String appendWorld(String s) {
        return s + ", World!";
    }

    public static void main(String[] args) {
        // fold
        final List.Cons<Integer> list123 = cons(1, cons(2, cons(3, nil())));
        System.out.println("The sum of " + list123 + " is " + list123.fold((next, acc) -> next + acc, 0));
        System.out.println("The first element of " + list123 + " is " + getHead(list123));

        // Id
        System.out.println("Id:");
        final Id<String> idHello = Id.of("Hello");
        System.out.println("'" + idHello + "'" + " is transformed into " + "'" + map(PatternMatching::appendWorld, idHello) + "'");

        // Optional
        System.out.println("=======================");
        System.out.println("Optional:");
        final Optional<String> optionalHello = some("Hello");
        final Optional<String> none = none();

        System.out.println("'" + optionalHello + "'" + " is transformed into " + "'" + map(PatternMatching::appendWorld, optionalHello) + "'");
        System.out.println("'" + none + "'" + " is transformed into " + "'" + map(PatternMatching::appendWorld, none) + "'");

        // List
        System.out.println("=======================");
        System.out.println("List:");
        final List<String> cons = cons("Hello", cons("Goodbye", nil()));
        final List<String> nil = nil();
        System.out.println("The first element of " + cons + " is " + getHead(cons));

        System.out.println("'" + cons  + "'"+ " is transformed into " + "'" + map(PatternMatching::appendWorld, cons) + "'");
        System.out.println("'" + nil  + "'"+ " is transformed into " + "'" + map(PatternMatching::appendWorld, nil) + "'");

        // Tree
        System.out.println("=======================");
        System.out.println("Tree:");
        System.out.println("   Hello      ");
        System.out.println("   /   \\      ");
        System.out.println("  ()  Goodbye ");
        System.out.println("      /     \\ ");
        System.out.println("     ()     ()");
        final Tree<String> tree = node(leaf(), "Hello", node(leaf(), "Goodbye", leaf()));
        final Tree<String> messagesTree = map(PatternMatching::appendWorld, tree);
        final Tree<String> messagesLeaf = map(PatternMatching::appendWorld, leaf());
        System.out.println("'" + tree + "' is transformed to '" + messagesTree + "'");
        System.out.println("'" + leaf() + "' is transformed to '" + messagesLeaf + "'");
    }

    private static<T> Optional<T> getHead(List<T> list) {
        return list.caseOf(
                () -> none(),
                (head, __) -> some(head)
        );
    }
}
