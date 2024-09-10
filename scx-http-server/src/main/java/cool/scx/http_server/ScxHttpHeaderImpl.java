package cool.scx.http_server;

import java.util.List;

public record ScxHttpHeaderImpl(ScxHttpHeaderName name, List<String> allValues) implements ScxHttpHeader {

}
