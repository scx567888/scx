package cool.scx.http.media.multi_part;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/// MultiPartImpl
///
/// @author scx567888
/// @version 0.0.1
public class MultiPartImpl implements MultiPartWritable {

    private final List<MultiPartPart> parts;
    private String boundary;

    public MultiPartImpl(String boundary) {
        this.boundary = boundary;
        this.parts = new ArrayList<>();
    }

    @Override
    public MultiPartWritable boundary(String boundary) {
        this.boundary = boundary;
        return this;
    }

    @Override
    public MultiPartWritable add(MultiPartPart part) {
        parts.add(part);
        return this;
    }

    @Override
    public String boundary() {
        return boundary;
    }

    @Override
    public Iterator<MultiPartPart> iterator() {
        return parts.iterator();
    }

}
