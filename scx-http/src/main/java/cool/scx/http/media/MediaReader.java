package cool.scx.http.media;

import cool.scx.http.ScxHttpHeaders;

import java.io.InputStream;

/**
 * 读取器 可用于 ServerRequest 和 ClientResponse
 *
 * @param <T>
 */
public interface MediaReader<T> {

    T read(InputStream inputStream, ScxHttpHeaders requestHeaders);

}
