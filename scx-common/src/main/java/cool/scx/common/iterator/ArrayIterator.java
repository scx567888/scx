package cool.scx.common.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayIterator<T> implements Iterator<T>, Iterable<T> {

    private final T[] array;
    private int index;

    public ArrayIterator(T[] array) {
        this.array = array;
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < array.length;
    }

    @Override
    public T next() {
        if (index >= array.length) {
            throw new NoSuchElementException("No more elements.");
        }
        var n = array[index];
        array[index] = null; // 可能有助于垃圾回收
        index = index + 1;
        return n;
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

}
