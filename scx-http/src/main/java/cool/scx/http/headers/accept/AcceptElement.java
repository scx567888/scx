package cool.scx.http.headers.accept;

import cool.scx.http.media_type.ScxMediaType;

import static cool.scx.http.headers.accept.AcceptHelper.decodeAccept;

/// Accept
///
/// @author scx567888
/// @version 0.0.1
public interface AcceptElement {

    static AcceptElement of(String accept) {
        if (accept == null) {
            return null;
        }
        return decodeAccept(accept);
    }

    ScxMediaType mediaType();

    Double q();

}
