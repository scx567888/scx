package cool.scx.util.cycle_iterable;

public class Node<E> {

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
