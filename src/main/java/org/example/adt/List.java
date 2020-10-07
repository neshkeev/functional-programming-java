package org.example.adt;

import org.example.kind.App;
import org.example.parser.ParserK;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public abstract class List<T> {
    private List() { }

    public<R> R fold(BiFunction<T, R, R> reducer, R initial) {
        return caseOf(
                () -> initial,
                (head, tail) -> reducer.apply(head, tail.fold(reducer, initial))
        );
    }

    public String string() {
        return fold((e, r) -> e + r, "");
    }

    public static final class Nil<T> extends List<T> { }

    public static final class Cons<T> extends List<T> {
        final T head;
        final List<T> tail;

        public Cons(T head, List<T> tail) {
            this.head = head;
            this.tail = tail;
        }

        public T getHead() {
            return head;
        }

        public List<T> getTail() {
            return tail;
        }
    }

    public static <T> Nil<T> nil() {
        return new Nil<>();
    }

    public static <T> Cons<T> singleton(T head) {
        return cons(head, nil());
    }
    public static <T> Cons<T> cons(T head, List<T> tail) {
        return new Cons<>(head, tail);
    }

    public List<T> concat(List<T> that) {
        return caseOf(
                () -> that,
                (head, tail) -> cons(head, tail.concat(that))
        );
    }

    public <R> R caseOf(
            Supplier<R> nilOp,
            BiFunction<T, List<T>, R> consOp
    ) {
        if (this instanceof Nil) {
            return nilOp.get();
        }
        Cons<T> cons = (Cons<T>) this;
        return consOp.apply(cons.getHead(), cons.getTail());
    }

    @Override
    public String toString() {
        return "[" + show() + "]";
    }

    private String show() {
        return caseOf(
                () -> "",
                (head, tail) -> head.toString() + ";" + tail.show()
        );
    }
}
