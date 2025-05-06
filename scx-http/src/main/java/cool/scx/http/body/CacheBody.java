package cool.scx.http.body;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;
import cool.scx.io.data_reader.DataReader;
import cool.scx.io.io_stream.DataReaderInputStream;
import cool.scx.io.io_stream.StreamClosedException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import static cool.scx.io.IOHelper.inputStreamToDataReader;

public class CacheBody implements ScxHttpBody {

    private final ScxHttpHeaders headers;
    private final DataReader dataReader;

    public CacheBody(InputStream inputStream, ScxHttpHeaders requestHeaders) {
        this.headers = requestHeaders;
        this.dataReader = inputStreamToDataReader(inputStream);
        this.dataReader.mark();
    }

    @Override
    public InputStream inputStream() {
        this.dataReader.reset();
        return new DataReaderInputStream(this.dataReader);
    }

    @Override
    public <T> T as(MediaReader<T> t) {
        try {
            return t.read(inputStream(), headers);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (StreamClosedException e) {
            throw new BodyAlreadyConsumedException();
        }
    }

}
