package cool.scx.collections.circular_linked_list;

import java.util.function.IntFunction;

/// ReverseCircularLinkedList
///
/// @param <T>
/// @author scx567888
/// @version 0.0.1
final class ReverseCircularLinkedList<T> implements ICircularLinkedList<T> {

    private final CircularLinkedList<T> circularLinkedList;

    public ReverseCircularLinkedList(CircularLinkedList<T> circularLinkedList) {
        this.circularLinkedList = circularLinkedList;
    }

    @Override
    public T first() {
        return circularLinkedList.last();
    }

    @Override
    public T last() {
        return circularLinkedList.first();
    }

    @Override
    public Node<T> firstNode() {
        return circularLinkedList.lastNode();
    }

    @Override
    public Node<T> lastNode() {
        return circularLinkedList.firstNode();
    }

    @Override
    public int size() {
        return circularLinkedList.size();
    }

    @Override
    public boolean add(T item) {
        return circularLinkedList.add(item);
    }

    @Override
    public boolean remove(Object o) {
        return circularLinkedList.remove(o);
    }

    @Override
    public void clear() {
        circularLinkedList.clear();
    }

    @Override
    public Node<T> node(Object o) {
        return circularLinkedList.node(o);
    }

    @Override
    public ICircularLinkedIterator<T> iterator() {
        return new ReverseCircularLinkedIterator<>(new CircularLinkedIterator<>(circularLinkedList.lastNode()));
    }

    @Override
    public Object[] toArray() {
        var arr = new Object[circularLinkedList.size()];
        fillArray(arr);
        return arr;
    }

    @Override
    public T[] toArray(IntFunction<T[]> generator) {
        var arr = generator.apply(circularLinkedList.size());
        fillArray(arr);
        return arr;
    }

    @Override
    public boolean isEmpty() {
        return circularLinkedList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return circularLinkedList.contains(o);
    }

    @Override
    public ICircularLinkedList<T> reversed() {
        return circularLinkedList;
    }

    private void fillArray(Object[] arr) {
        int i = 0;
        var x = circularLinkedList.lastNode();
        do {
            arr[i] = x.item;
            i = i + 1;
            x = x.prev;
        }
        while (x != circularLinkedList.lastNode());
    }

} 
