package cool.scx.http.content_type;

import cool.scx.http.Parameters;
import cool.scx.http.ScxMediaType;

public class ContentTypeImpl implements ContentTypeWritable {

    private ScxMediaType mediaType;
    private Parameters<String, String> params;

    @Override
    public ContentTypeWritable mediaType(ScxMediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public ContentTypeWritable params(Parameters<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public ScxMediaType mediaType() {
        return mediaType;
    }

    @Override
    public Parameters<String, String> params() {
        return params;
    }

}