package cool.scx.http.headers.accept;

import cool.scx.http.media_type.ScxMediaType;

/// AcceptImpl
///
/// @author scx567888
/// @version 0.0.1
public class AcceptElementImpl implements AcceptElementWritable {

    private ScxMediaType mediaType;
    private Double q;

    public AcceptElementImpl() {
        this.mediaType = null;
        this.q = 1.0;
    }

    @Override
    public AcceptElementWritable mediaType(ScxMediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public AcceptElementWritable q(Double q) {
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
