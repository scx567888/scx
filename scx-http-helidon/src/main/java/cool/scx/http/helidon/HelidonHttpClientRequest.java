package cool.scx.http.helidon;

import cool.scx.http.HttpMethod;
import cool.scx.http.ScxHttpClientRequestBase;
import cool.scx.http.ScxHttpClientResponse;
import io.helidon.http.HeaderNames;
import io.helidon.http.Method;
import io.helidon.webclient.api.WebClient;

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
    public HelidonHttpClientResponse send() {
        var r = webClient.method(Method.create(method.value()));
        r.uri(uri.toString());
        for (var h : headers) {
            r.header(HeaderNames.create(h.getKey().value()), h.getValue());
        }
        var res =  r.request();
        return new HelidonHttpClientResponse(res);
    }

    @Override
    public ScxHttpClientResponse send(Object body) {
        var r = webClient.method(Method.create(method.value()));
        r.uri(uri.toString());
        for (var h : headers) {
            r.header(HeaderNames.create(h.getKey().value()), h.getValue());
        }
        var res = r.submit(body);
        return new HelidonHttpClientResponse(res);
    }

}
