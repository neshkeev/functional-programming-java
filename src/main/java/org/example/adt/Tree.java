package org.example.adt;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Tree<T> {
    private Tree() {}
    public static final class Leaf<T> extends Tree<T> {
        @Override
        public String toString() { return "()"; }
    }
    public static final class Node<T> extends Tree<T> {
        final Tree<T> left;
        final T value;
        final Tree<T> right;

        public Node(Tree<T> left, T value, Tree<T> right) {
            this.left = left;
            this.value = value;
            this.right = right;
        }

        public Tree<T> getLeft() { return left; }

        public T getValue() { return value; }

        public Tree<T> getRight() { return right; }

        @Override
        public String toString() {
            return "(" + left.toString() + " " + value.toString() + " " + right.toString() + ")";
        }
    }

    public static <T> Leaf<T> leaf() { return new Leaf<>(); }
    public static <T> Node<T> node(Tree<T> left, T value, Tree<T> right) { return new Node<>(left, value, right); }

    public <R> R caseOf(
            Supplier<R> leafOp,
            Function<Tree<T>, Function<T, Function<Tree<T>, R>>> nodeOp
    ) {
        if (this instanceof Leaf) return leafOp.get();
        Node<T> node = (Node<T>) this;
        return nodeOp.apply(node.getLeft()).apply(node.getValue()).apply(node.getRight());
    }
}
