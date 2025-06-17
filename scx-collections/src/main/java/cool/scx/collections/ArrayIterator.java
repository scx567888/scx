package cool.scx.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

/// 迭代器
public class ArrayIterator<T> implements Iterator<T> {

    private final T[] array;
    private int index;

    public ArrayIterator(T... array) {
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
        index = index + 1;
        return n;
    }

}
