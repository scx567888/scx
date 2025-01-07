package cool.scx.common.circular_linked_list;

import java.util.Objects;
import java.util.function.IntFunction;

/**
 * CircularLinkedList
 *
 * @param <T>
 * @author scx567888
 * @version 0.0.1
 */
public final class CircularLinkedList<T> implements ICircularLinkedList<T> {

    private Node<T> first;

    private Node<T> last;

    private int size;

    @Override
    public T first() {
        return first.item;
    }

    @Override
    public T last() {
        return last.item;
    }

    @Override
    public Node<T> firstNode() {
        return first;
    }

    @Override
    public Node<T> lastNode() {
        return last;
    }

    @Override
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

    @Override
    public boolean add(T item) {
        var node = new Node<>(item);
        if (last == null) {
            first = last = node.prev = node.next = node;
        } else {
            link(node);
        }
        size = size + 1;
        return true;
    }

    @Override
    public boolean remove(Object o) {
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

    @Override
    public void clear() {
        var x = first;
        while (x != null) {
            var next = x.next;
            x.item = null;
            x.next = null;
            x.prev = null;
            x = next;
        }
        first = last = null;
        size = 0;
    }

    @Override
    public Node<T> node(Object o) {
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
    public CircularLinkedListIterator<T> iterator() {
        return new CircularLinkedListIterator<>(first);
    }

    private void fillArray(Object[] arr) {
        int i = 0;
        var x = first;
        do {
            arr[i] = x.item;
            i = i + 1;
            x = x.next;
        }
        while (x != first);
    }

    @Override
    public Object[] toArray() {
        var arr = new Object[size];
        fillArray(arr);
        return arr;
    }

    @Override
    public T[] toArray(IntFunction<T[]> generator) {
        var arr = generator.apply(size);
        fillArray(arr);
        return arr;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        var node = node(o);
        return node != null;
    }

    @Override
    public ICircularLinkedList<T> reversed() {
        return new ReverseCircularLinkedList<>(this);
    }

}
