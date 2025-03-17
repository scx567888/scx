package cool.scx.http.headers.accept;

import cool.scx.http.media_type.ScxMediaType;

/// AcceptWritable
///
/// @author scx567888
/// @version 0.0.1
public interface AcceptElementWritable extends AcceptElement {

    AcceptElementWritable mediaType(ScxMediaType mediaType);

    AcceptElementWritable q(Double q);

}
