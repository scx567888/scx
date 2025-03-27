package cool.scx.http.media;

import cool.scx.http.headers.ScxHttpHeaders;

import java.io.InputStream;

/// 读取器 可用于 ServerRequest 和 ClientResponse
///
/// @param <T>
/// @author scx567888
/// @version 0.0.1
public interface MediaReader<T> {

    /// 读取内容
    ///
    /// @param inputStream    输入流
    /// @param requestHeaders 请求头 (在客户端状态下是 responseHeaders)
    T read(InputStream inputStream, ScxHttpHeaders requestHeaders);

}
