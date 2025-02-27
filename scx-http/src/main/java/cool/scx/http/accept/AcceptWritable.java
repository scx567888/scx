package cool.scx.http.accept;

import cool.scx.http.ScxMediaType;

/// AcceptWritable
///
/// @author scx567888
/// @version 0.0.1
public interface AcceptWritable extends Accept {

    AcceptWritable mediaType(ScxMediaType mediaType);

    AcceptWritable q(Double q);

}
