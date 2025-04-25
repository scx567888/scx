package cool.scx.http;

import cool.scx.http.headers.ScxHttpHeadersWriteHelper;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.method.ScxHttpMethod;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.ScxURIWritable;
import cool.scx.http.version.HttpVersion;

/// ScxHttpClientRequest
///
/// @author scx567888
/// @version 0.0.1
public interface ScxHttpClientRequest extends ScxHttpSender<ScxHttpClientResponse>, ScxHttpHeadersWriteHelper<ScxHttpClientRequest> {

    HttpVersion version();

    ScxHttpMethod method();

    ScxURIWritable uri();

    @Override
    ScxHttpHeadersWritable headers();

    ScxHttpClientRequest version(HttpVersion version);

    ScxHttpClientRequest method(ScxHttpMethod method);

    ScxHttpClientRequest uri(ScxURI uri);

    ScxHttpClientRequest headers(ScxHttpHeaders headers);

    @Override
    ScxHttpClientResponse send(MediaWriter writer);

    //******************** 简化操作 *******************

    default ScxHttpClientRequest method(String method) {
        return method(ScxHttpMethod.of(method));
    }

    default ScxHttpClientRequest uri(String uri) {
        return uri(ScxURI.of(uri));
    }

}
