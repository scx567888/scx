package cool.scx.common.util.circular_iterable;

import java.util.function.IntFunction;

public interface ICircularIterable<T> extends Iterable<T> {

    T first();

    T last();

    Node<T> firstNode();

    Node<T> lastNode();

    int size();

    boolean add(T item);

    boolean remove(Object o);

    void clear();

    Node<T> node(Object o);

    @Override
    ICircularIterator<T> iterator();

    Object[] toArray();

    T[] toArray(IntFunction<T[]> generator);

    boolean isEmpty();

    boolean contains(Object o);

    ICircularIterable<T> reversed();

}
