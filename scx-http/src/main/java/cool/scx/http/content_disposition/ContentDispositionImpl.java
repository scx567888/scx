package cool.scx.http.content_disposition;

import cool.scx.http.Parameters;
import cool.scx.http.ParametersWritable;

/**
 * ContentDispositionImpl
 *
 * @author scx567888
 * @version 0.0.1
 */
public class ContentDispositionImpl implements ContentDispositionWritable {

    private String type;
    private ParametersWritable<String, String> params;

    public ContentDispositionImpl() {
        this.type = null;
        this.params = Parameters.of();
    }

    public ContentDispositionImpl(ContentDisposition oldContentDisposition) {
        this.type(oldContentDisposition.type());
        this.params(oldContentDisposition.params());
    }

    @Override
    public ContentDispositionWritable type(String type) {
        this.type = type;
        return this;
    }

    @Override
    public ContentDispositionWritable params(Parameters<String, String> params) {
        this.params = Parameters.of(params);
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

    @Override
    public String toString() {
        return encode();
    }

}
