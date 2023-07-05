package cool.scx.util;

import java.util.Iterator;

/**
 * 无限循环的迭代器
 *
 * @param <T>
 */
public final class CycleIterator<T> implements Iterator<T> {

    private Node<T> currentNode;
    private Node<T> firstNode;

    @Override
    public boolean hasNext() {
        return currentNode != null;
    }

    /**
     * 调用 next 会进行无限循环的 next
     *
     * @return a
     */
    @Override
    public T next() {
        var currentItem = currentNode.item;
        currentNode = currentNode.next;
        return currentItem;
    }

    public void add(T item) {
        if (currentNode == null) {
            currentNode = new Node<>(item);
            firstNode = currentNode;
        } else {
            currentNode.next = new Node<>(item);
            currentNode = currentNode.next;
        }
        currentNode.next = firstNode;
    }

    private static class Node<E> {
        private final E item;
        private Node<E> next;

        private Node(E item) {
            this.item = item;
        }
    }

}
