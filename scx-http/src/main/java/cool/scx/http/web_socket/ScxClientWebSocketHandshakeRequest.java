package cool.scx.http.web_socket;

import cool.scx.http.ScxHttpClientRequest;
import cool.scx.http.media.MediaWriter;

/**
 * ScxClientWebSocketBuilder
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface ScxClientWebSocketHandshakeRequest extends ScxHttpClientRequest {

    @Override
    default ScxClientWebSocketHandshakeResponse send(MediaWriter writer) {
        throw new UnsupportedOperationException("Not supported Body.");
    }

    @Override
    ScxClientWebSocketHandshakeResponse send();

}
