package cool.scx.common.circular_linked_list;

/// 无限循环的迭代器 (倒转)
///
/// @param <T>
/// @author scx567888
/// @version 0.0.1
final class ReverseCircularLinkedIterator<T> implements ICircularLinkedIterator<T> {

    private final CircularLinkedIterator<T> circularLinkedIterator;

    ReverseCircularLinkedIterator(CircularLinkedIterator<T> circularLinkedIterator) {
        this.circularLinkedIterator = circularLinkedIterator;
    }

    @Override
    public boolean hasNext() {
        return circularLinkedIterator.hasNext();
    }

    @Override
    public T next() {
        return circularLinkedIterator.prev();
    }

    @Override
    public T prev() {
        return circularLinkedIterator.next();
    }

    @Override
    public Node<T> nextNode() {
        return circularLinkedIterator.prevNode();
    }

    @Override
    public Node<T> prevNode() {
        return circularLinkedIterator.nextNode();
    }

}
