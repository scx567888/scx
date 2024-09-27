package cool.scx.http.media;

import cool.scx.http.ScxHttpHeaders;

import java.io.InputStream;

/**
 * 媒体读取器 一般用来从 ServerRequest 或 ClientResponse 中读取数据
 *
 * @param <T>
 */
public interface MediaReader<T> {

    T read(InputStream inputStream, ScxHttpHeaders headers);

}
