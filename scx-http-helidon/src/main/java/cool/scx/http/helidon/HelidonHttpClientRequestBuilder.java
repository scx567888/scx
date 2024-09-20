package cool.scx.http.helidon;

import cool.scx.http.HttpMethod;
import cool.scx.http.ScxHttpClientRequestBuilderBase;
import io.helidon.http.HeaderNames;
import io.helidon.http.Method;
import io.helidon.webclient.api.WebClient;

/**
 * HelidonHttpClientRequestBuilder
 */
public class HelidonHttpClientRequestBuilder extends ScxHttpClientRequestBuilderBase {

    private final WebClient webClient;

    public HelidonHttpClientRequestBuilder(WebClient webClient) {
        this.webClient = webClient;
        this.method = HttpMethod.GET;
    }

    @Override
    public HelidonHttpClientResponse request() {
        var r = webClient.method(Method.create(method.value()));
        r.uri(uri.toString());
        for (var h : headers) {
            r.header(HeaderNames.create(h.getKey().value()), h.getValue());
        }
        var res = body != null ? r.submit(body) : r.request();
        return new HelidonHttpClientResponse(res);
    }

}
