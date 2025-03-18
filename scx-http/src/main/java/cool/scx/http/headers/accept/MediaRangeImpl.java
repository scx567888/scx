package cool.scx.http.headers.accept;

import cool.scx.http.parameters.Parameters;
import cool.scx.http.parameters.ParametersWritable;

/// AcceptImpl
///
/// @author scx567888
/// @version 0.0.1
public class MediaRangeImpl implements MediaRangeWritable {

    private String type;
    private String subtype;
    private ParametersWritable<String, String> params;

    public MediaRangeImpl() {
        this.type = "*";
        this.subtype = "*";
        this.params = Parameters.of();
    }

    @Override
    public MediaRangeWritable type(String type) {
        this.type = type;
        return this;
    }

    @Override
    public MediaRangeWritable subtype(String subtype) {
        this.subtype = subtype;
        return this;
    }

    @Override
    public MediaRangeWritable params(Parameters<String, String> params) {
        this.params = Parameters.of(params);
        return this;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public String subtype() {
        return subtype;
    }

    @Override
    public ParametersWritable<String, String> params() {
        return params;
    }

}
