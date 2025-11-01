package cool.scx.http.x.http1;

import cool.scx.http.body.BodyAlreadyConsumedException;
import cool.scx.http.body.BodyReadException;
import cool.scx.http.body.ScxHttpBody;
import cool.scx.http.media.MediaReader;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.io.ByteInput;
import cool.scx.io.exception.AlreadyClosedException;
import cool.scx.io.exception.ScxIOException;

/// ScxHttpBodyImpl
///
/// @author scx567888
/// @version 0.0.1
public record Http1Body(ByteInput byteInput, Http1Headers headers) implements ScxHttpBody {

    @Override
    public <T> T as(MediaReader<T> t) throws BodyAlreadyConsumedException {
        try {
            return t.read(byteInput, headers);
        } catch (ScxIOException e) {
            throw new BodyReadException(e);
        } catch (AlreadyClosedException e) {
            throw new BodyAlreadyConsumedException();
        }
    }

}
