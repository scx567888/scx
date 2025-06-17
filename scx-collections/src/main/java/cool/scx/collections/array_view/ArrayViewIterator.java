package cool.scx.collections.array_view;

import java.util.ListIterator;
import java.util.NoSuchElementException;

/// 迭代器
public class ArrayViewIterator<T> implements ListIterator<T> {

    private final T[] array;
    private int index;

    public ArrayViewIterator(T[] array, int index) {
        this.array = array;
        this.index = index;
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

    @Override
    public boolean hasPrevious() {
        return index > 0;
    }

    @Override
    public T previous() {
        if (index <= 0) {
            throw new NoSuchElementException("No more elements.");
        }
        var n = array[index - 1];
        index = index - 1;
        return n;
    }

    @Override
    public int nextIndex() {
        return index;
    }

    @Override
    public int previousIndex() {
        return index - 1;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(T t) {
        throw new UnsupportedOperationException();
    }

}
