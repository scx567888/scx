package cool.scx.http;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.easy.ScxHttpHeadersReader;
import cool.scx.http.status.ScxHttpStatus;

/// ScxHttpClientResponse
///
/// @author scx567888
/// @version 0.0.1
public interface ScxHttpClientResponse extends ScxHttpHeadersReader {

    ScxHttpStatus status();

    ScxHttpHeaders headers();

    ScxHttpBody body();

}
