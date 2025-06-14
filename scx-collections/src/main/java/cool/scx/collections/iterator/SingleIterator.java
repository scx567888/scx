package cool.scx.collections.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/// 迭代器
public class SingleIterator<T> implements Iterator<T>, Iterable<T> {

    private T next;
    private boolean hasNext;

    public SingleIterator(T t) {
        this.next = t;
        this.hasNext = true;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public T next() {
        if (!hasNext) {
            throw new NoSuchElementException("No more elements.");
        }
        var n = next;
        next = null; // 可能有助于垃圾回收
        hasNext = false;
        return n;
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

}
