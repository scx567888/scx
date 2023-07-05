package cool.scx.util;

import java.util.Iterator;

/**
 * 无限循环的迭代器
 *
 * @param <T>
 */
public final class CycleIterator<T> implements Iterator<T> {

    private Node<T> last;
    private Node<T> first;

    @Override
    public boolean hasNext() {
        return last != null;
    }

    /**
     * 调用 next 会进行无限循环的 next
     *
     * @return a
     */
    @Override
    public T next() {
        var item = last.item;
        last = last.next;
        return item;
    }

    public void add(T item) {
        if (last == null) {
            last = new Node<>(item);
            first = last;
        } else {
            last.next = new Node<>(item);
            last = last.next;
        }
        last.next = first;
    }

    private static class Node<E> {
        private final E item;
        private Node<E> next;

        private Node(E item) {
            this.item = item;
        }
    }

}
