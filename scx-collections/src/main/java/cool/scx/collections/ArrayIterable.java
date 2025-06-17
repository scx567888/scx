package cool.scx.collections;

import java.util.Iterator;

/// 可迭代的
public class ArrayIterable<T> implements Iterable<T> {

    private final T[] array;

    public ArrayIterable(T... array) {
        this.array = array;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator<>(array);
    }

}
