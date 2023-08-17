package cool.scx.util.cycle;

import java.util.Objects;

public class CycleIterable<T> implements Iterable<T> {

    private Node<T> first;

    private Node<T> last;

    private int size;

    public Node<T> first() {
        return first;
    }

    public Node<T> last() {
        return last;
    }

    public int size() {
        return size;
    }

    private void link(Node<T> node) {
        node.prev = last;
        node.next = first;
        last.next = node;
        first.prev = node;
        last = node;
    }

    private T unlink(Node<T> node) {
        var item = node.item;
        var next = node.next;
        var prev = node.prev;
        next.prev = prev;
        prev.next = next;
        if (node == first) {
            first = next;
        }
        if (node == last) {
            last = prev;
        }
        node.next = null;
        node.prev = null;
        node.item = null;
        return item;
    }

    public void add(T item) {
        var node = new Node<>(item);
        if (last == null) {
            first = last = node.prev = node.next = node;
        } else {
            link(node);
        }
        size = size + 1;
    }

    public boolean remove(T o) {
        var node = node(o);
        if (node == null) {
            return false;
        }
        unlink(node);
        size = size - 1;
        if (size == 0) {
            first = last = null;
        }
        return true;
    }

    public Node<T> node(T o) {
        if (first == null) {
            return null;
        }
        var x = first;
        do {
            if (Objects.equals(o, x.item)) {
                return x;
            }
            x = x.next;
        }
        while (x != first);
        return null;
    }

    @Override
    public CycleIterator<T> iterator() {
        return new CycleIterator<>(first);
    }

    public CycleReverseIterable<T> reverse() {
        return new CycleReverseIterable<>(this);
    }

}
