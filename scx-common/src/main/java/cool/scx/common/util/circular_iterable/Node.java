package cool.scx.common.util.circular_iterable;

public final class Node<E> {

    E item;

    Node<E> next;

    Node<E> prev;

    Node(E item) {
        this.item = item;
    }

    public E item() {
        return item;
    }

    public Node<E> next() {
        return next;
    }

    public Node<E> prev() {
        return prev;
    }

}
