package cool.scx.common.util.circular_iterable;

import java.util.Iterator;

public interface ICircularIterator<T> extends Iterator<T> {

    @Override
    boolean hasNext();

    @Override
    T next();

    T prev();

    Node<T> nextNode();

    Node<T> prevNode();

}
