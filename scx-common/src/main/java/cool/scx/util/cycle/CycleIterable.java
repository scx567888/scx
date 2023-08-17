package cool.scx.util.cycle;

public class CycleIterable<T> implements Iterable<T> {

    private Node<T> first;

    private Node<T> last;

    private int size;

    public Node<T> first() {
        return first;
    }

    public Node<T> last() {
        return last;
    }

    public int size() {
        return size;
    }

    private void link(Node<T> node) {
        node.prev = last;
        node.next = first;
        last.next = node;
        first.prev = node;
        last = node;
    }

    private T unlink(Node<T> node) {
        var item = node.item;
        var next = node.next;
        var prev = node.prev;
        next.prev = prev;
        prev.next = next;
        node.next = null;
        node.prev = null;
        node.item = null;
        return item;
    }

    public void add(T item) {
        var node = new Node<>(item);
        if (last == null) {
            first = last = node.prev = node.next = node;
        } else {
            link(node);
        }
        size = size + 1;
    }

    public boolean remove(T o) {
        var x = first;
        while (x != null) {
            if (o.equals(x.item)) {
                unlink(x);
                size = size - 1;
                return true;
            }
            x = x.next;
        }
        return false;
    }

    @Override
    public CycleIterator<T> iterator() {
        return new CycleIterator<>(first);
    }

    public CycleReverseIterable<T> reverse() {
        return new CycleReverseIterable<>(this);
    }

}
