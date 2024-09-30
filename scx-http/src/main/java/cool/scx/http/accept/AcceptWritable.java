package cool.scx.http.accept;

import cool.scx.http.ScxMediaType;

public interface AcceptWritable extends Accept {

    AcceptWritable mediaType(ScxMediaType mediaType);

    AcceptWritable q(Double q);

}
