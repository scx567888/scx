package cool.scx.http.headers.accept;

import cool.scx.http.ScxMediaType;

/// AcceptImpl
///
/// @author scx567888
/// @version 0.0.1
public class AcceptImpl implements AcceptWritable {

    private ScxMediaType mediaType;
    private Double q;

    public AcceptImpl() {
        this.mediaType = null;
        this.q = 1.0;
    }

    @Override
    public AcceptWritable mediaType(ScxMediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public AcceptWritable q(Double q) {
        this.q = q;
        return this;
    }

    @Override
    public ScxMediaType mediaType() {
        return mediaType;
    }

    @Override
    public Double q() {
        return q;
    }

}
