package cool.scx.http.x.content_codec;

import cool.scx.http.headers.content_encoding.ScxContentEncoding;

import java.io.InputStream;
import java.io.OutputStream;

public interface HttpContentCodec {

    /// 是否能够处理
    boolean canHandle(ScxContentEncoding contentEncoding);

    /// 解码
    InputStream decode(InputStream inputStream);

    /// 编码
    OutputStream encode(OutputStream outputStream);

}
