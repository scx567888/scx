package cool.scx.http.helidon;

import cool.scx.http.ScxHttpHeaderName;
import cool.scx.http.ScxHttpHeadersWritable;
import io.helidon.http.HeaderNames;
import io.helidon.http.ServerResponseHeaders;

class HelidonHttpHeadersWritable extends HelidonHttpHeaders<ServerResponseHeaders> implements ScxHttpHeadersWritable {

    public HelidonHttpHeadersWritable(ServerResponseHeaders headers) {
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

}
