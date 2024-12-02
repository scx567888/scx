package cool.scx.common.circular_iterable;

import java.util.Iterator;

/**
 * ICircularIterator
 * @param <T>
 * @author scx567888
 * @version 1.11.8
 */
public interface ICircularIterator<T> extends Iterator<T> {

    @Override
    boolean hasNext();

    @Override
    T next();

    T prev();

    Node<T> nextNode();

    Node<T> prevNode();

}
