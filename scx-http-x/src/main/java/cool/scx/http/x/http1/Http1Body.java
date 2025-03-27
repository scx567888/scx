package cool.scx.http.x.http1;

import cool.scx.http.ScxHttpBody;
import cool.scx.http.media.MediaReader;
import cool.scx.http.x.http1.headers.Http1Headers;

import java.io.InputStream;

/// ScxHttpBodyImpl
///
/// @author scx567888
/// @version 0.0.1
public record Http1Body(InputStream inputStream, Http1Headers headers) implements ScxHttpBody {

    @Override
    public String toString() {
        return asString();
    }

    @Override
    public <T> T as(MediaReader<T> t) {
        return t.read(inputStream, headers);
    }

}
