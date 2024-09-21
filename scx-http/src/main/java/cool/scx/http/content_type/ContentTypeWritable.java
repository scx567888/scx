package cool.scx.http.content_type;

import cool.scx.http.Parameters;
import cool.scx.http.ScxMediaType;

public interface ContentTypeWritable extends ContentType {

    ContentTypeWritable mediaType(ScxMediaType mediaType);

    ContentTypeWritable params(Parameters<String, String> parameters);

}
