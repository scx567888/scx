package cool.scx.http.headers.accept;

import cool.scx.http.media_type.ScxMediaType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/// AcceptsImpl
///
/// @author scx567888
/// @version 0.0.1
public class AcceptImpl implements AcceptWritable {

    private final List<AcceptElement> acceptElements;

    public AcceptImpl(Accept oldAccepts) {
        this.acceptElements = new ArrayList<>();
        for (var oldAccept : oldAccepts) {
            this.acceptElements.add(oldAccept);
        }
    }

    public AcceptImpl() {
        this.acceptElements = new ArrayList<>();
    }

    @Override
    public long size() {
        return acceptElements.size();
    }

    @Override
    public boolean contains(ScxMediaType accept) {
        for (var a : acceptElements) {
            if (a.mediaType().equals(accept)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<AcceptElement> iterator() {
        return acceptElements.iterator();
    }

    @Override
    public AcceptWritable add(AcceptElement acceptElement) {
        this.acceptElements.add(acceptElement);
        return this;
    }

    @Override
    public AcceptWritable remove(AcceptElement acceptElement) {
        this.acceptElements.remove(acceptElement);
        return this;
    }

}
