package cool.scx.http;

import cool.scx.http.media.FormParams;
import cool.scx.http.media.MultiPart;

import java.io.InputStream;

/**
 * ScxHttpBody
 */
public interface ScxHttpBody {

    InputStream inputStream();

    <T> T as(Class<T> t);

    default byte[] asBytes() {
        return as(byte[].class);
    }

    default String asString() {
        return as(String.class);
    }

    default FormParams asFormParams() {
        return as(FormParams.class);
    }

    default MultiPart asMultiPart() {
        return as(MultiPart.class);
    }

}
