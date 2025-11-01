package cool.scx.http.media.string;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;
import cool.scx.io.ByteInput;
import cool.scx.io.exception.AlreadyClosedException;
import cool.scx.io.exception.ScxIOException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/// 1, 如果未指定字符集则 使用 请求头中的字符集 如果请求头中的字符集为空则回退到 UTF_8
/// 2, 如果指定字符集 忽略 请求头中的字符集
///
/// @author scx567888
/// @version 0.0.1
public final class StringReader implements MediaReader<String> {

    /// 常用情况 特殊情况直接 new
    public static final StringReader STRING_READER = new StringReader();

    /// 用户指定的 charset
    private final Charset charset;

    public StringReader(Charset charset) {
        this.charset = charset;
    }

    private StringReader() {
        this.charset = null;
    }

    public static Charset getContentTypeCharsetOrUTF8(ScxHttpHeaders headers) {
        var contentType = headers.contentType();
        if (contentType != null) {
            var charset = contentType.charset();
            if (charset != null) {
                return charset;
            }
        }
        return StandardCharsets.UTF_8;
    }

    @Override
    public String read(ByteInput byteInput, ScxHttpHeaders headers) throws ScxIOException, AlreadyClosedException {
        // 如果用户没有指定编码 我们尝试查找 ContentType 中的编码
        var c = charset != null ? charset : getContentTypeCharsetOrUTF8(headers);

        try (byteInput) {
            var bytes = byteInput.readAll();
            return new String(bytes, c);
        }
    }

}
