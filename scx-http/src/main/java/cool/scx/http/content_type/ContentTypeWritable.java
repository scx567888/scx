package cool.scx.http.content_type;

import cool.scx.http.ParametersWritable;
import cool.scx.http.ScxMediaType;

import java.nio.charset.Charset;

public interface ContentTypeWritable extends ContentType {

    ContentTypeWritable mediaType(ScxMediaType mediaType);

    ContentTypeWritable params(ParametersWritable<String, String> parameters);

    ParametersWritable<String, String> params();

    default ContentTypeWritable charset(Charset c) {
        params().set("charset", c.name());
        return this;
    }

}
