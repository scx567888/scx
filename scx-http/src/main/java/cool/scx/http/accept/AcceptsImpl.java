package cool.scx.http.accept;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AcceptsImpl implements AcceptsWritable {

    private final List<Accept> accepts;

    public AcceptsImpl() {
        this.accepts = new ArrayList<>();
    }

    @Override
    public long size() {
        return accepts.size();
    }

    @Override
    public Iterator<Accept> iterator() {
        return accepts.iterator();
    }

    @Override
    public AcceptsWritable add(Accept accept) {
        this.accepts.add(accept);
        return this;
    }

    @Override
    public AcceptsWritable remove(Accept accept) {
        this.accepts.remove(accept);
        return this;
    }

}
