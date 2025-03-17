package cool.scx.http.headers.accept;

import cool.scx.http.media_type.ScxMediaType;

/// Accepts
///
/// @author scx567888
/// @version 0.0.1
public interface Accept extends Iterable<AcceptElement> {

    static AcceptWritable of(String acceptsStr) {
        return AcceptHelper.decodeAccepts(acceptsStr);
    }

    static AcceptWritable of(Accept oldAccept) {
        return new AcceptImpl(oldAccept);
    }

    long size();
    
    boolean contains(ScxMediaType mediaType);

}
