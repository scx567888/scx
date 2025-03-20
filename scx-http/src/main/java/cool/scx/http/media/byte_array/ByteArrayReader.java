package cool.scx.http.media.byte_array;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/// 保持单例模式
///
/// @author scx567888
/// @version 0.0.1
public final class ByteArrayReader implements MediaReader<byte[]> {

    public static final ByteArrayReader BYTE_ARRAY_READER = new ByteArrayReader();

    private ByteArrayReader() {

    }

    @Override
    public byte[] read(InputStream inputStream, ScxHttpHeaders headers) {
        try (inputStream) {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            // 这里能出现 IO 异常的情况只可能是 连接关闭 所以不应该抛出 客户端异常
            throw new UncheckedIOException(e);
        }
    }

}
