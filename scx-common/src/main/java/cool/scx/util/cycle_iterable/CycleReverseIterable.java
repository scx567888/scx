package cool.scx.util.cycle_iterable;

import java.util.Iterator;

public final class CycleReverseIterable<T> implements Iterable<T> {

    private final CycleIterable<T> cycleIterable;

    public CycleReverseIterable(CycleIterable<T> cycleIterable) {
        this.cycleIterable = cycleIterable;
    }

    @Override
    public Iterator<T> iterator() {
        return new CycleReverseIterator<>(cycleIterable.last());
    }

} 
