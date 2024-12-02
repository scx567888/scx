package cool.scx.common.circular_iterable;

/**
 * Node
 * @param <E>
 * @author scx567888
 * @version 1.11.8
 */
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
