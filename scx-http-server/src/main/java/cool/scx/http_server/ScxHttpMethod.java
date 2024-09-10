package cool.scx.http_server;

public sealed interface ScxHttpMethod permits ScxHttpMethodEnum, ScxHttpMethodImpl {

    String value();

}
