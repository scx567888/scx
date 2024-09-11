package cool.scx.http_server;

public interface ScxHttpRequest {

    ScxHttpMethod method();

    ScxHttpPath path();

    HttpVersion version();

    ScxHttpHeaders headers();

    ScxHttpBody body();

    ScxHttpResponse response();

}
