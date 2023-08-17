package cool.scx.util.cycle_iterable;

import java.lang.reflect.Array;
import java.util.Iterator;
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
    public CycleIterator<T> iterator() {
        return new CycleIterator<>(first);
    }

    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        var x = first;
        do {
            result[i] = x.item;
            i = i + 1;
            x = x.next;
        }
        while (x != first);
        return result;
    }

    @SuppressWarnings("unchecked")
    public <E> E[] toArray(E[] a) {
        if (a.length < size) {
            a = (E[]) Array.newInstance(a.getClass().getComponentType(), size);
        }
        Object[] result = a;
        int i = 0;
        var x = first;
        do {
            result[i] = x.item;
            i = i + 1;
            x = x.next;
        }
        while (x != first);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(Object o) {
        var node = node(o);
        return node != null;
    }

    public CycleReverseIterable<T> reverse() {
        return new CycleReverseIterable<>(this);
    }

}
