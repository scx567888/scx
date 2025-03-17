package cool.scx.http.media_type;

import cool.scx.http.headers.content_type.ContentTypeWritable;
import cool.scx.http.parameters.Parameters;
import cool.scx.http.parameters.ParametersWritable;

/// ScxMediaType
///
/// @author scx567888
/// @version 0.0.1
public interface ScxMediaTypeWritable extends ScxMediaType {

    ScxMediaTypeWritable type(String type);

    ScxMediaTypeWritable subtype(String type);

    ContentTypeWritable params(Parameters<String, String> parameters);

    @Override
    ParametersWritable<String, String> params();

}
