package cool.scx.http.media.string;

import cool.scx.http.ScxHttpServerRequestHeaders;
import cool.scx.http.media.MediaReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 1, 如果未指定字符集则 使用 请求头中的字符集 如果请求头中的字符集为空则回退到 UTF_8
 * 2, 如果指定字符集 忽略 请求头中的字符集
 */
public final class StringReader implements MediaReader<String> {

    /**
     * 常用情况 特殊情况直接 new
     */
    public static final StringReader STRING_READER = new StringReader();

    /**
     * 用户指定的 charset
     */
    private final Charset charset;

    public StringReader(Charset charset) {
        this.charset = charset;
    }

    public StringReader() {
        this.charset = null;
    }

    public static Charset getContentTypeCharsetOrUTF8(ScxHttpServerRequestHeaders headers) {
        var contentType = headers.contentType();
        var charset = contentType.charset();
        return charset == null ? StandardCharsets.UTF_8 : charset;
    }

    @Override
    public String read(InputStream inputStream, ScxHttpServerRequestHeaders headers) {
        var c = charset != null ? charset : getContentTypeCharsetOrUTF8(headers);
        try {
            var bytes = inputStream.readAllBytes();
            return new String(bytes, c);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
