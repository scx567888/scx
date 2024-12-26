package cool.scx.common.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SingleIterator<T> implements Iterator<T> {

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
        if (!hasNext()) {
            throw new NoSuchElementException("No more elements.");
        }
        var n = next;
        next = null; // 可能有助于垃圾回收
        hasNext = false;
        return n;
    }

}
