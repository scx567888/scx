package cool.scx.common.circular_iterable;

/**
 * 无限循环的迭代器 (倒转)
 *
 * @param <T>
 * @author scx567888
 * @version 1.11.8
 */
final class ReverseCircularIterator<T> implements ICircularIterator<T> {

    private final CircularIterator<T> circularIterator;

    ReverseCircularIterator(CircularIterator<T> circularIterator) {
        this.circularIterator = circularIterator;
    }

    @Override
    public boolean hasNext() {
        return circularIterator.hasNext();
    }

    @Override
    public T next() {
        return circularIterator.prev();
    }

    @Override
    public T prev() {
        return circularIterator.next();
    }

    @Override
    public Node<T> nextNode() {
        return circularIterator.prevNode();
    }

    @Override
    public Node<T> prevNode() {
        return circularIterator.nextNode();
    }

}
