package cool.scx.http.helidon;

import cool.scx.http.ScxHttpClientResponseHeaders;
import io.helidon.http.ClientResponseHeaders;

public class HelidonHttpClientResponseHeaders extends HelidonHttpHeaders<ClientResponseHeaders> implements ScxHttpClientResponseHeaders {

    public HelidonHttpClientResponseHeaders(ClientResponseHeaders headers) {
        super(headers);
    }

}
