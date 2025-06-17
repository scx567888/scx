package cool.scx.collections;

import java.util.Iterator;

/// 可迭代的
public class ArrayIterable<T> implements Iterable<T> {

    private final T[] array;
    private final int start;
    private final int end;

    public ArrayIterable(T[] array) {
        this(array, 0, array.length);
    }

    public ArrayIterable(T[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator<>(array, start, end);
    }

}
