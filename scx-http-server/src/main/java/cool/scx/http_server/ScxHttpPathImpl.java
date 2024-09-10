package cool.scx.http_server;

public record ScxHttpPathImpl(ScxHttpPathQuery query, String value, String rawValue) implements ScxHttpPath {

}
