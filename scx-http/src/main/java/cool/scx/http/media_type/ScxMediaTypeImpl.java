package cool.scx.http.media_type;

import cool.scx.http.parameters.Parameters;
import cool.scx.http.parameters.ParametersWritable;

/// ScxMediaType
///
/// @author scx567888
/// @version 0.0.1
public class ScxMediaTypeImpl implements ScxMediaTypeWritable {

    private String type;
    private String subtype;
    private ParametersWritable<String, String> params;

    public ScxMediaTypeImpl() {
        this.type = null;
        this.subtype = null;
        this.params = Parameters.of();
    }

    public ScxMediaTypeImpl(String type, String subtype, Parameters<String, String> params) {
        type(type);
        subtype(subtype);
        params(params);
    }

    @Override
    public ScxMediaTypeWritable type(String type) {
        this.type = type;
        return this;
    }

    @Override
    public ScxMediaTypeWritable subtype(String subtype) {
        this.subtype = subtype;
        return this;
    }

    @Override
    public ScxMediaTypeWritable params(Parameters<String, String> params) {
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

    @Override
    public String toString() {
        return encode();
    }

}
