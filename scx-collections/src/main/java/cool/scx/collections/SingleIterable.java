package cool.scx.collections;

import java.util.Iterator;

/// 迭代器
public class SingleIterable<T> implements Iterable<T> {

    private final T t;

    public SingleIterable(T t) {
        this.t = t;
    }

    @Override
    public Iterator<T> iterator() {
        return new SingleIterator<>(t);
    }

}
