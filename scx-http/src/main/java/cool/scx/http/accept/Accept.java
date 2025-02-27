package cool.scx.http.accept;

import cool.scx.http.ScxMediaType;

import static cool.scx.http.accept.AcceptHelper.decodeAccept;

/// Accept
///
/// @author scx567888
/// @version 0.0.1
public interface Accept {

    static Accept of(String accept) {
        if (accept == null) {
            return null;
        }
        return decodeAccept(accept);
    }

    ScxMediaType mediaType();

    Double q();

}
