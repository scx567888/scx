package cool.scx.util.cycle_iterable;

import java.util.Iterator;

/**
 * 无限循环的迭代器
 *
 * @param <T>
 */
public final class CycleIterator<T> implements Iterator<T> {

    private Node<T> now;

    CycleIterator(Node<T> now) {
        this.now = now;
    }

    @Override
    public boolean hasNext() {
        return now != null;
    }

    @Override
    public T next() {
        var item = now.item;
        now = now.next;
        return item;
    }

    public T prev() {
        var item = now.item;
        now = now.prev;
        return item;
    }

    public Node<T> nextNode() {
        var node = now;
        now = now.next;
        return node;
    }

    public Node<T> prevNode() {
        var node = now;
        now = now.next;
        return node;
    }

}
