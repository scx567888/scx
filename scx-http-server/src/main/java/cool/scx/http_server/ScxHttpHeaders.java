package cool.scx.http_server;

public interface ScxHttpHeaders extends Iterable<ScxHttpHeader> {

    ScxHttpHeader get(ScxHttpHeaderName headerName);

}
