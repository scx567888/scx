package cool.scx.http.content_disposition;

import cool.scx.http.Parameters;
import cool.scx.http.ParametersWritable;

public class ContentDispositionImpl implements ContentDispositionWritable {

    private String type;
    private ParametersWritable<String, String> params;

    public ContentDispositionImpl() {
        this.type = null;
        this.params = Parameters.of();
    }

    @Override
    public ContentDispositionWritable type(String type) {
        this.type = type;
        return this;
    }

    @Override
    public ContentDispositionWritable params(ParametersWritable<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public ParametersWritable<String, String> params() {
        return params;
    }

    @Override
    public String encode() {
        return ContentDispositionHelper.encodeContentDisposition(this);
    }

}
