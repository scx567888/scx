package cool.scx.http.body;

import cool.scx.io.ByteInput;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;
import cool.scx.io.ByteInputMark;
import cool.scx.io.exception.AlreadyClosedException;
import cool.scx.io.exception.ScxIOException;

import java.io.IOException;

//todo 如何处理 close ?
// 问题可以看 XTest.java
public class CacheBody implements ScxHttpBody {

    private final ScxHttpHeaders headers;
    private final ByteInput byteInput;
    private final ByteInputMark mark;

    public CacheBody(ByteInput byteInput, ScxHttpHeaders requestHeaders) {
        this.headers = requestHeaders;
        this.byteInput = byteInput;
        this.mark = this.byteInput.mark();
    }

    @Override
    public ByteInput byteInput() {
        mark.reset();
        return this.byteInput;
    }

    @Override
    public <T> T as(MediaReader<T> t) throws BodyAlreadyConsumedException, BodyReadException {
        try {
            return t.read(byteInput(), headers);
        } catch (ScxIOException e) {
            throw new BodyReadException(e);
        } catch (AlreadyClosedException e) {
            throw new BodyAlreadyConsumedException();
        }
    }

    @Override
    public CacheBody asCacheBody() {
        return this;
    }

}
