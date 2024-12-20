package cool.scx.http.helidon;

import cool.scx.http.HttpMethod;
import cool.scx.http.ScxHttpClientRequestBase;
import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.media.MediaWriter;
import io.helidon.http.HeaderNames;
import io.helidon.http.Method;
import io.helidon.webclient.api.WebClient;

/**
 * HelidonHttpClientRequestBuilder
 *
 * @author scx567888
 * @version 0.0.1
 */
class HelidonHttpClientRequest extends ScxHttpClientRequestBase {

    private final WebClient webClient;
    private final HelidonHttpClient client;

    public HelidonHttpClientRequest(WebClient webClient, HelidonHttpClient client) {
        this.webClient = webClient;
        this.method = HttpMethod.GET;
        this.client = client;
    }

    @Override
    public HelidonHttpClientResponse send(MediaWriter writer) {
        var r = webClient.method(Method.create(method.value()));
        //这里已经转换为 URL 编码了 无需再转换一遍
        r.uri(uri.toURI());
        writer.beforeWrite(headers, ScxHttpHeaders.of());
        for (var h : headers) {
            r.header(HeaderNames.create(h.getKey().value()), h.getValue());
        }
        var httpClientResponse = r.outputStream(writer::write);
        return new HelidonHttpClientResponse(httpClientResponse, this.client);
    }

}
