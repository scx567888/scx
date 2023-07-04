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
            currentNode = new Node<>();
            currentNode.item = item;
            currentNode.next = currentNode;
            firstNode = currentNode;
        } else {
            var node = new Node<T>();
            node.item = item;
            currentNode.next = node;
            currentNode = node;
            currentNode.next = firstNode;
        }
    }

    private static class Node<E> {
        private E item;
        private Node<E> next;
    }

}
