package cool.scx.http.media;

import cool.scx.http.ScxHttpHeaders;

import java.io.InputStream;

/// 读取器 可用于 ServerRequest 和 ClientResponse
///
/// @param <T>
/// @author scx567888
/// @version 0.0.1
public interface MediaReader<T> {

    T read(InputStream inputStream, ScxHttpHeaders requestHeaders);

}
