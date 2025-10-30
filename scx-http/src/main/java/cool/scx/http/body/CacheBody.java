package cool.scx.http.body;

import cool.scx.io.ByteInput;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;
import cool.scx.io.ByteInputMark;
import cool.scx.io.x.io_stream.ByteReaderInputStream;
import cool.scx.io.x.io_stream.StreamClosedException;

import java.io.IOException;
import java.io.InputStream;

import static cool.scx.io.x.IOHelper.inputStreamToByteReader;

public class CacheBody implements ScxHttpBody {

    private final ScxHttpHeaders headers;
    private final ByteInput dataReader;
    private final ByteInputMark mark;

    public CacheBody(InputStream inputStream, ScxHttpHeaders requestHeaders) {
        this.headers = requestHeaders;
        this.dataReader = inputStreamToByteReader(inputStream);
        this.mark = this.dataReader.mark();
    }

    @Override
    public InputStream inputStream() {
        mark.reset();
        return new ByteReaderInputStream(this.dataReader);
    }

    @Override
    public <T> T as(MediaReader<T> t) throws BodyAlreadyConsumedException, BodyReadException {
        try {
            return t.read(inputStream(), headers);
        } catch (IOException e) {
            throw new BodyReadException(e);
        } catch (StreamClosedException e) {
            throw new BodyAlreadyConsumedException();
        }
    }

    @Override
    public CacheBody asCacheBody() {
        return this;
    }

}
