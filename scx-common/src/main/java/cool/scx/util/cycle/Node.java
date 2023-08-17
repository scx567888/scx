package cool.scx.util.cycle;

class Node<E> {

    E item;
    Node<E> next;
    Node<E> prev;

    Node(E item) {
        this.item = item;
    }

}
