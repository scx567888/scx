package cool.scx.http.media_type;

import cool.scx.http.parameters.Parameters;

/// ScxMediaType
///
/// @author scx567888
/// @version 0.0.1
public interface ScxMediaType {

    String type();

    String subtype();

    Parameters<String, String> params();

    default String encode() {
        return ScxMediaTypeHelper.encodeMediaType(this);
    }

}
