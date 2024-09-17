package cool.scx.http;

import java.io.InputStream;

/**
 * ScxHttpBody
 */
public interface ScxHttpBody {

    InputStream inputStream();

    byte[] asBytes();

    String asString();

    <T> T as(Class<T> t);

}
