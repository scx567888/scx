package cool.scx.http.body;

import cool.scx.http.exception.UnsupportedMediaTypeException;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.content_encoding.ScxContentEncoding;
import cool.scx.http.media.MediaReader;
import cool.scx.io.ByteInput;
import cool.scx.io.adapter.ByteInputAdapter;
import cool.scx.io.exception.AlreadyClosedException;

import java.io.IOException;
import java.util.zip.GZIPInputStream;

import static cool.scx.http.headers.content_encoding.ContentEncoding.GZIP;

public class GzipBody implements ScxHttpBody {

    private final ScxHttpHeaders headers;
    private final ByteInput inputStream;

    public GzipBody(ByteInput byteInput, ScxHttpHeaders requestHeaders) {
        this.headers = requestHeaders;
        this.inputStream = initInputStream(byteInput, this.headers.contentEncoding());
    }

    public static ByteInput initInputStream(ByteInput inputStream, ScxContentEncoding contentEncoding) {
        //已经包装过一次 没必要重复包装
        if (inputStream instanceof GZIPInputStream) {
            return inputStream;
        }
        //没有 contentEncoding 直接返回原始流
        if (contentEncoding == null) {
            return inputStream;
        }
        //等于 GZIP 我们尝试包装
        if (contentEncoding == GZIP) {
            try {
                return ByteInputAdapter.inputStreamToByteInput(new GZIPInputStream(ByteInputAdapter.byteInputToInputStream(inputStream)));
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
        return inputStream;
    }

    @Override
    public <T> T as(MediaReader<T> t) throws BodyReadException, BodyAlreadyConsumedException {
        try {
            return t.read(inputStream, headers);
        } catch (IOException e) {
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
