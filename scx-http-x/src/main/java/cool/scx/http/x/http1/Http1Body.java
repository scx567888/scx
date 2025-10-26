package cool.scx.http.x.http1;

import cool.scx.http.body.BodyAlreadyConsumedException;
import cool.scx.http.body.BodyReadException;
import cool.scx.http.body.ScxHttpBody;
import cool.scx.http.media.MediaReader;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.io.x.io_stream.StreamClosedException;

import java.io.IOException;
import java.io.InputStream;

/// ScxHttpBodyImpl
///
/// @author scx567888
/// @version 0.0.1
public record Http1Body(InputStream inputStream, Http1Headers headers) implements ScxHttpBody {

    @Override
    public <T> T as(MediaReader<T> t) throws BodyAlreadyConsumedException {
        try {
            return t.read(inputStream, headers);
        } catch (IOException e) {
            throw new BodyReadException(e);
        } catch (StreamClosedException e) {
            throw new BodyAlreadyConsumedException();
        }
    }

}
