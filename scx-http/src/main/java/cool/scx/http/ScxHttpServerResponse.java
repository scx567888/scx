package cool.scx.http;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.headers.ScxHttpHeadersWriteHelper;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.status.ScxHttpStatus;

/// ScxHttpServerResponse
///
/// @author scx567888
/// @version 0.0.1
public interface ScxHttpServerResponse extends ScxHttpSender<Void>, ScxHttpHeadersWriteHelper<ScxHttpServerResponse> {

    ScxHttpServerRequest request();

    ScxHttpStatus status();

    @Override
    ScxHttpHeadersWritable headers();

    ScxHttpServerResponse status(ScxHttpStatus code);

    ScxHttpServerResponse headers(ScxHttpHeaders headers);

    boolean isSent();

    @Override
    Void send(MediaWriter writer);

    //******************** 简化操作 *******************

    default ScxHttpServerResponse status(int code) {
        return status(ScxHttpStatus.of(code));
    }

}
