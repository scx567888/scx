package cool.scx.http.media.event_stream;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;

import java.io.InputStream;

public class ClientEventStreamReader implements MediaReader<ClientEventStream> {

    @Override
    public ClientEventStream read(InputStream inputStream, ScxHttpHeaders requestHeaders) {
        return new ClientEventStream(inputStream);
    }
    
}
