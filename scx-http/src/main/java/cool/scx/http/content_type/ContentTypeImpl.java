package cool.scx.http.content_type;

import cool.scx.http.Parameters;
import cool.scx.http.ParametersWritable;
import cool.scx.http.ScxMediaType;

public class ContentTypeImpl implements ContentTypeWritable {

    private ScxMediaType mediaType;
    private ParametersWritable<String, String> params;

    public ContentTypeImpl() {
        this.mediaType = null;
        this.params = Parameters.of();
    }

    @Override
    public ContentTypeWritable mediaType(ScxMediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public ContentTypeWritable params(ParametersWritable<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public ScxMediaType mediaType() {
        return mediaType;
    }

    @Override
    public ParametersWritable<String, String> params() {
        return params;
    }

    @Override
    public String encode() {
        return ContentTypeHelper.encodeContentType(this);
    }

    @Override
    public String toString() {
        return encode();
    }
    
}
