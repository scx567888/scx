package cool.scx.common.collection;

import cool.scx.common.iterator.ArrayIterator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public class ReadOnlyArrayCollection<E> implements Collection<E> {

    private final E[] elements;

    public ReadOnlyArrayCollection(E[] elements) {
        this.elements = elements;
    }

    @Override
    public int size() {
        return elements.length;
    }

    @Override
    public boolean isEmpty() {
        return elements.length == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (E element : elements) {
            if (Objects.equals(element, o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator<>(elements);
    }

    @Override
    public E[] toArray() {
        return elements;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (var e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        int size = elements.length;
        if (a.length < size) {
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(elements, size, a.getClass());
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null; // null-terminate
        }
        return a;
    }


    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
}
