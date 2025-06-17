package cool.scx.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

/// 迭代器
public class SingleIterator<T> implements Iterator<T> {

    private final T next;
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
        if (!hasNext()) {
            throw new NoSuchElementException("No more elements.");
        }
        hasNext = false;
        return next;
    }

}
