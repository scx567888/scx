package cool.scx.http.accept;

import cool.scx.http.ScxMediaType;

import static cool.scx.http.accept.AcceptHelper.decodeAccept;

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
