package cool.scx.http.body;

import cool.scx.io.ByteInput;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;
import cool.scx.io.DefaultByteInput;
import cool.scx.io.exception.AlreadyClosedException;
import cool.scx.io.exception.ScxIOException;
import cool.scx.io.supplier.CacheByteSupplier;

//todo 如何处理 close ?
// 问题可以看 XTest.java
public class CacheBody implements ScxHttpBody {

    private final ScxHttpHeaders headers;
    private final ByteInput byteInput;
    private final CacheByteSupplier cache;

    public CacheBody(ByteInput byteInput, ScxHttpHeaders requestHeaders) {
        this.headers = requestHeaders;
        this.byteInput = byteInput;
        this.cache = new CacheByteSupplier(byteInput);
    }

    @Override
    public ByteInput byteInput() {
        cache.reset();
        return new DefaultByteInput(cache);
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
