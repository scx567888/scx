package cool.scx.http.body;

import cool.scx.io.ByteInput;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;
import cool.scx.io.ByteInputMark;
import cool.scx.io.adapter.ByteInputAdapter;
import cool.scx.io.exception.AlreadyClosedException;

import java.io.IOException;
import java.io.InputStream;

public class CacheBody implements ScxHttpBody {

    private final ScxHttpHeaders headers;
    private final ByteInput dataReader;
    private final ByteInputMark mark;

    public CacheBody(ByteInput inputStream, ScxHttpHeaders requestHeaders) {
        this.headers = requestHeaders;
        this.dataReader = inputStream;
        this.mark = this.dataReader.mark();
    }

    @Override
    public ByteInput byteInput() {
        mark.reset();
        return this.dataReader;
    }

    @Override
    public <T> T as(MediaReader<T> t) throws BodyAlreadyConsumedException, BodyReadException {
        try {
            return t.read(byteInput(), headers);
        } catch (IOException e) {
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
