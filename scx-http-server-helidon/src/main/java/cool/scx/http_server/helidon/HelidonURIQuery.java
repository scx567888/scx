package cool.scx.http_server.helidon;

import cool.scx.http_server.URIQuery;
import io.helidon.common.uri.UriQuery;

import java.util.List;

public class HelidonURIQuery implements URIQuery {

    private final UriQuery uriQuery;

    public HelidonURIQuery(UriQuery uriQuery) {
        this.uriQuery = uriQuery;
    }

    @Override
    public String get(String name) {
        return uriQuery.get(name);
    }

    @Override
    public List<String> getAll(String name) {
        return uriQuery.all(name);
    }
    
}
