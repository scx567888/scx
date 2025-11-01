package cool.scx.http.body;

import cool.scx.http.exception.UnsupportedMediaTypeException;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.content_encoding.ScxContentEncoding;
import cool.scx.http.media.MediaReader;
import cool.scx.io.ByteInput;
import cool.scx.io.adapter.ByteInputAdapter;
import cool.scx.io.exception.AlreadyClosedException;
import cool.scx.io.exception.ScxIOException;

import java.io.IOException;
import java.util.zip.GZIPInputStream;

import static cool.scx.http.headers.content_encoding.ContentEncoding.GZIP;

public class GzipBody implements ScxHttpBody {

    private final ScxHttpHeaders headers;
    private final ByteInput gzipByteInput;

    public GzipBody(ByteInput byteInput, ScxHttpHeaders requestHeaders) {
        this.headers = requestHeaders;
        this.gzipByteInput = intGZIPByteInput(byteInput, this.headers.contentEncoding());
    }

    public static ByteInput intGZIPByteInput(ByteInput byteInput, ScxContentEncoding contentEncoding) {
        // todo 这里 检测包装有问题
        //已经包装过一次 没必要重复包装
        if (byteInput instanceof GZIPInputStream) {
            return byteInput;
        }
        //没有 contentEncoding 直接返回原始流
        if (contentEncoding == null) {
            return byteInput;
        }
        //等于 GZIP 我们尝试包装
        if (contentEncoding == GZIP) {
            try {
                return ByteInputAdapter.inputStreamToByteInput(new GZIPInputStream(ByteInputAdapter.byteInputToInputStream(byteInput)));
            } catch (IOException e) {
                //原始流有可能并不是一个 合法的 gzip 流 我们抛出异常
                throw new UnsupportedMediaTypeException(e);
            }
        } else {// 否则我们不支持这种类型 抛出异常
            throw new UnsupportedMediaTypeException("Unsupported Content-Encoding: " + contentEncoding);
        }
    }

    @Override
    public ByteInput byteInput() {
        return gzipByteInput;
    }

    @Override
    public <T> T as(MediaReader<T> t) throws BodyReadException, BodyAlreadyConsumedException {
        try {
            return t.read(gzipByteInput, headers);
        } catch (ScxIOException e) {
            throw new BodyReadException(e);
        } catch (AlreadyClosedException e) {
            throw new BodyAlreadyConsumedException();
        }
    }

    @Override
    public GzipBody asGzipBody() {
        return this;
    }

}
