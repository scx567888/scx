package cool.scx.http.helidon;

import cool.scx.http.ScxHttpHeaderName;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.ScxHttpServerResponseHeaders;
import cool.scx.http.cookie.Cookie;
import io.helidon.http.HeaderNames;
import io.helidon.http.ServerResponseHeaders;
import io.helidon.http.SetCookie;

import java.time.Duration;

class HelidonHttpServerResponseHeaders extends HelidonHttpHeaders<ServerResponseHeaders> implements ScxHttpServerResponseHeaders {

    public HelidonHttpServerResponseHeaders(ServerResponseHeaders headers) {
        super(headers);
    }

    @Override
    public ScxHttpHeadersWritable set(ScxHttpHeaderName headerName, String... headerValue) {
        return set(headerName.value(), headerValue);
    }

    @Override
    public ScxHttpHeadersWritable set(String headerName, String... headerValue) {
        headers.set(HeaderNames.create(headerName), headerValue);
        return this;
    }

    @Override
    public ScxHttpHeadersWritable add(ScxHttpHeaderName headerName, String... headerValue) {
        return add(headerName.value(), headerValue);
    }

    @Override
    public ScxHttpHeadersWritable add(String headerName, String... headerValue) {
        headers.add(HeaderNames.create(headerName), headerValue);
        return this;
    }

    @Override
    public ScxHttpHeadersWritable remove(ScxHttpHeaderName headerName) {
        return remove(headerName.value());
    }

    @Override
    public ScxHttpHeadersWritable remove(String headerName) {
        headers.remove(HeaderNames.create(headerName));
        return this;
    }

    @Override
    public ScxHttpServerResponseHeaders addCookie(Cookie cookie) {
        var c = SetCookie
                .builder(cookie.name(), cookie.name())
                .domain(cookie.domain())
                .path(cookie.path())
                .maxAge(Duration.ofSeconds(cookie.maxAge()))
                .secure(cookie.secure())
                .httpOnly(cookie.httpOnly())
                .sameSite(SetCookie.SameSite.valueOf(cookie.sameSite().name()))
                .build();
        this.headers.addCookie(c);
        return null;
    }

    @Override
    public ScxHttpServerResponseHeaders removeCookie(String name) {
        this.headers.clearCookie(name);
        return this;
    }

}
