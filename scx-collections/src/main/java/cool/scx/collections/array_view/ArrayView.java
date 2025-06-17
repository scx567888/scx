package cool.scx.collections.array_view;

import cool.scx.collections.ArrayIterator;

import java.util.*;

public class ArrayView<E> implements List<E> {

    private final E[] elements;

    public ArrayView(E... elements) {
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
        if (o == null) {
            for (E e : elements) {
                if (e == null) {
                    return true;
                }
            }
        } else {
            for (E e : elements) {
                if (o.equals(e)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator<>(elements);
    }

    @Override
    public Object[] toArray() {
        return elements;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < elements.length) {
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(elements, elements.length, a.getClass());
        }
        System.arraycopy(elements, 0, a, 0, elements.length);
        if (a.length > elements.length) {
            a[elements.length] = null;
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
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
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

    @Override
    public E get(int index) {
        return elements[index];
    }

    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < elements.length; i = i + 1) {
                if (elements[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < elements.length; i = i + 1) {
                if (o.equals(elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = elements.length - 1; i >= 0; i = i - 1) {
                if (elements[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = elements.length - 1; i >= 0; i = i - 1) {
                if (o.equals(elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ArrayViewIterator<>(elements, 0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ArrayViewIterator<>(elements, index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return List.of();
    }

}
