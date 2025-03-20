package cool.scx.http.media.event_stream;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;

import java.io.InputStream;

import static cool.scx.http.media.string.StringReader.getContentTypeCharsetOrUTF8;

public class ClientEventStreamReader implements MediaReader<ClientEventStream> {

    public static final ClientEventStreamReader CLIENT_EVENT_STREAM_READER = new ClientEventStreamReader();

    private ClientEventStreamReader() {

    }

    @Override
    public ClientEventStream read(InputStream inputStream, ScxHttpHeaders requestHeaders) {
        // EventStream 是有字符集的概念的 所以这里需要读取字符集
        var charset = getContentTypeCharsetOrUTF8(requestHeaders);
        return new ClientEventStream(inputStream, charset);
    }

}
