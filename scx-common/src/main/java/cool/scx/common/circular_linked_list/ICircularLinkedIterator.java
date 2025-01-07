package cool.scx.common.circular_linked_list;

import java.util.Iterator;

/**
 * ICircularLinkedIterator
 *
 * @param <T>
 * @author scx567888
 * @version 0.0.1
 */
public interface ICircularLinkedIterator<T> extends Iterator<T> {

    @Override
    boolean hasNext();

    @Override
    T next();

    T prev();

    Node<T> nextNode();

    Node<T> prevNode();

}
