package cool.scx.http.helidon;

import cool.scx.http.ScxHttpServerRequestHeaders;
import cool.scx.http.cookie.Cookies;
import io.helidon.http.ServerRequestHeaders;

/**
 * HelidonHttpHeaders
 */
class HelidonHttpServerRequestHeaders extends HelidonHttpHeaders<ServerRequestHeaders> implements ScxHttpServerRequestHeaders {

    private final HelidonCookies cookies;

    public HelidonHttpServerRequestHeaders(ServerRequestHeaders headers) {
        super(headers);
        this.cookies = new HelidonCookies(headers.cookies());
    }

    @Override
    public Cookies cookies() {
        return cookies;
    }

}
