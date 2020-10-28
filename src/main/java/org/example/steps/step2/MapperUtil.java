package org.example.steps.step2;

import org.example.adt.Id;
import org.example.adt.List;
import org.example.adt.Optional;
import org.example.adt.Tree;

import java.util.function.Function;

import static org.example.adt.List.cons;
import static org.example.adt.Tree.node;

public class MapperUtil {
    private MapperUtil() {}
    static <T, E> Id<E>       map(Function<T, E> mapper, Id<T> in) {
        return in.caseOf(mapper.andThen(Id::of));
    }
    static <T, E> Optional<E> map(Function<T, E> mapper, Optional<T> in) {
        return in.caseOf(
                Optional::none,
                s -> mapper.andThen(Optional::some).apply(s)
        );
    }
    static <T, E> List<E>     map(Function<T, E> mapper, List<T> in) {
        return in.caseOf(
                List::nil,
                (head, tail) -> cons(mapper.apply(head), map(mapper, tail))
        );
    }
    static <T, E> Tree<E>     map(Function<T, E> mapper, Tree<T> in) {
        return in.caseOf(
                Tree::leaf,
                left -> value -> right -> node(map(mapper, left), mapper.apply(value), map(mapper, right))
        );
    }
}
