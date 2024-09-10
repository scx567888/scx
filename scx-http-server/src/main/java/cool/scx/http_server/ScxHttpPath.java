package cool.scx.http_server;

public interface ScxHttpPath {

    ScxHttpPathQuery query();

    String value();

    String rawValue();

}
