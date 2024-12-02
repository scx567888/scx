package cool.scx.common.circular_iterable;

/**
 * 无限循环的迭代器
 *
 * @param <T>
 * @author scx567888
 * @version 0.0.1
 */
public final class CircularIterator<T> implements ICircularIterator<T> {

    private Node<T> now;

    CircularIterator(Node<T> now) {
        this.now = now;
    }

    @Override
    public boolean hasNext() {
        return now != null;
    }

    @Override
    public T next() {
        var item = now.item;
        now = now.next;
        return item;
    }

    @Override
    public T prev() {
        var item = now.item;
        now = now.prev;
        return item;
    }

    @Override
    public Node<T> nextNode() {
        var node = now;
        now = now.next;
        return node;
    }

    @Override
    public Node<T> prevNode() {
        var node = now;
        now = now.next;
        return node;
    }

}
