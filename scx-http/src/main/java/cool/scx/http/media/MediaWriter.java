package cool.scx.http.media;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;

import java.io.OutputStream;

/// 写入器 可用于 ServerResponse 和 ClientRequest
///
/// @author scx567888
/// @version 0.0.1
public interface MediaWriter {

    /// 写入内容之前 在这里可以设置 header 头
    ///
    /// @param responseHeaders 响应头 (在客户端状态下是 requestHeaders)
    /// @param requestHeaders  请求头 (在客户端状态下是 空 Header)
    void beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders);

    /// 写入内容
    ///
    /// @param outputStream 输入流
    void write(OutputStream outputStream);

}
