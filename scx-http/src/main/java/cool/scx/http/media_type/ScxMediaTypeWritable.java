package cool.scx.http.media_type;

import cool.scx.http.parameters.Parameters;
import cool.scx.http.parameters.ParametersWritable;

import java.nio.charset.Charset;

/// ScxMediaTypeWritable
///
/// @author scx567888
/// @version 0.0.1
public interface ScxMediaTypeWritable extends ScxMediaType {

    ScxMediaTypeWritable type(String type);

    ScxMediaTypeWritable subtype(String subtype);

    ScxMediaTypeWritable params(Parameters<String, String> params);

    @Override
    ParametersWritable<String, String> params();

    default ScxMediaTypeWritable charset(Charset c) {
        params().set("charset", c.name());
        return this;
    }

    default ScxMediaTypeWritable boundary(String boundary) {
        params().set("boundary", boundary);
        return this;
    }

}
