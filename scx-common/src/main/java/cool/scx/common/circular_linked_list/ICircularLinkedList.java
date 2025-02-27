package cool.scx.common.circular_linked_list;

import java.util.function.IntFunction;

/// ICircularLinkedList
///
/// @param <T>
/// @author scx567888
/// @version 0.0.1
public interface ICircularLinkedList<T> extends Iterable<T> {

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
    ICircularLinkedIterator<T> iterator();

    Object[] toArray();

    T[] toArray(IntFunction<T[]> generator);

    boolean isEmpty();

    boolean contains(Object o);

    ICircularLinkedList<T> reversed();

}
