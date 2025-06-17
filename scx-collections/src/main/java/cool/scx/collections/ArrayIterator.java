package cool.scx.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

/// 迭代器
public class ArrayIterator<T> implements Iterator<T> {

    private final T[] array;
    private final int end;
    private int index;

    public ArrayIterator(T[] array) {
        this(array, 0, array.length);
    }

    public ArrayIterator(T[] array, int start, int end) {
        this.array = array;
        this.end = end;
        this.index = start;
    }

    @Override
    public boolean hasNext() {
        return index < end;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more elements.");
        }
        var n = array[index];
        index = index + 1;
        return n;
    }

}
