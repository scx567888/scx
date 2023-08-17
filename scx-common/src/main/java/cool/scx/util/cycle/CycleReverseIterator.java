package cool.scx.util.cycle;

import java.util.Iterator;

/**
 * 无限循环的迭代器 (倒转)
 *
 * @param <T>
 */
public final class CycleReverseIterator<T> implements Iterator<T> {

    private Node<T> now;

    CycleReverseIterator(Node<T> now) {
        this.now = now;
    }

    @Override
    public boolean hasNext() {
        return now != null;
    }

    @Override
    public T next() {
        var item = now.item;
        now = now.prev;
        return item;
    }

    public T prev() {
        var item = now.item;
        now = now.next;
        return item;
    }

}
