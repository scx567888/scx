package cool.scx.http.content_type;

import cool.scx.http.Parameters;
import cool.scx.http.ParametersWritable;
import cool.scx.http.ScxMediaType;

import java.nio.charset.Charset;

/// ContentTypeWritable
///
/// @author scx567888
/// @version 0.0.1
public interface ContentTypeWritable extends ContentType {

    ContentTypeWritable mediaType(ScxMediaType mediaType);

    ContentTypeWritable params(Parameters<String, String> parameters);

    ParametersWritable<String, String> params();

    default ContentTypeWritable charset(Charset c) {
        params().set("charset", c.name());
        return this;
    }

    default ContentTypeWritable boundary(String boundary) {
        params().set("boundary", boundary);
        return this;
    }

}
