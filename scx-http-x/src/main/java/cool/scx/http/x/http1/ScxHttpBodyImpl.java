package cool.scx.http.x.http1;

import cool.scx.http.ScxHttpBody;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;

import java.io.InputStream;

/// ScxHttpBodyImpl
///
/// @author scx567888
/// @version 0.0.1
public record ScxHttpBodyImpl(InputStream inputStream, ScxHttpHeaders headers) implements ScxHttpBody {

    @Override
    public String toString() {
        return asString();
    }

    @Override
    public <T> T as(MediaReader<T> t) {
        return t.read(inputStream, headers);
    }

}
