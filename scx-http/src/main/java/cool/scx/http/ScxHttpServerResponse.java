package cool.scx.http;

import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.status.ScxHttpStatus;

/// ScxHttpServerResponse
///
/// @author scx567888
/// @version 0.0.1
public interface ScxHttpServerResponse extends ScxHttpSender<Void>, ScxHttpHeadersWriter<ScxHttpServerResponse> {

    ScxHttpServerRequest request();

    ScxHttpStatus status();

    ScxHttpHeadersWritable headers();

    ScxHttpServerResponse status(ScxHttpStatus code);

    boolean isSent();

    //******************** 简化操作 *******************

    default ScxHttpServerResponse status(int code) {
        return status(ScxHttpStatus.of(code));
    }

}
