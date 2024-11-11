package cool.scx.common.circular_iterable;

import java.util.function.IntFunction;

final class ReverseCircularIterable<T> implements ICircularIterable<T> {

    private final CircularIterable<T> cycleIterable;

    public ReverseCircularIterable(CircularIterable<T> cycleIterable) {
        this.cycleIterable = cycleIterable;
    }

    @Override
    public T first() {
        return cycleIterable.last();
    }

    @Override
    public T last() {
        return cycleIterable.first();
    }

    @Override
    public Node<T> firstNode() {
        return cycleIterable.lastNode();
    }

    @Override
    public Node<T> lastNode() {
        return cycleIterable.firstNode();
    }

    @Override
    public int size() {
        return cycleIterable.size();
    }

    @Override
    public boolean add(T item) {
        return cycleIterable.add(item);
    }

    @Override
    public boolean remove(Object o) {
        return cycleIterable.remove(o);
    }

    @Override
    public void clear() {
        cycleIterable.clear();
    }

    @Override
    public Node<T> node(Object o) {
        return cycleIterable.node(o);
    }

    @Override
    public ICircularIterator<T> iterator() {
        return new ReverseCircularIterator<>(new CircularIterator<>(cycleIterable.lastNode()));
    }

    @Override
    public Object[] toArray() {
        return cycleIterable.toArray();
    }

    @Override
    public T[] toArray(IntFunction<T[]> generator) {
        return cycleIterable.toArray(generator);
    }

    @Override
    public boolean isEmpty() {
        return cycleIterable.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return cycleIterable.contains(o);
    }

    @Override
    public ICircularIterable<T> reversed() {
        return cycleIterable;
    }

} 
