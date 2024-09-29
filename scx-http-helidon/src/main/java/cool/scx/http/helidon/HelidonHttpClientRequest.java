package cool.scx.http.helidon;

import cool.scx.http.HttpMethod;
import cool.scx.http.ScxHttpClientRequestBase;
import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.media.MediaWriter;
import io.helidon.http.HeaderNames;
import io.helidon.http.Method;
import io.helidon.webclient.api.WebClient;

import java.net.URI;

/**
 * HelidonHttpClientRequestBuilder
 */
public class HelidonHttpClientRequest extends ScxHttpClientRequestBase {

    private final WebClient webClient;

    public HelidonHttpClientRequest(WebClient webClient) {
        this.webClient = webClient;
        this.method = HttpMethod.GET;
    }

    @Override
    public HelidonHttpClientResponse send(MediaWriter writer) {
        var r = webClient.method(Method.create(method.value()));
        //这里已经转换为 URL 编码了 无需再转换一遍
        r.uri(URI.create(uri.encode()));
        writer.beforeWrite(headers, ScxHttpHeaders.of());
        for (var h : headers) {
            r.header(HeaderNames.create(h.getKey().value()), h.getValue());
        }
        var httpClientResponse = r.outputStream(writer::write);
        return new HelidonHttpClientResponse(httpClientResponse);
    }

}
