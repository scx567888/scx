package cool.scx.http_server;

public interface ScxHttpRequest {

    ScxHttpMethod method();

    ScxHttpPath path();

    ScxHttpVersion version();

    ScxHttpHeaders headers();

    ScxHttpBody body();

    ScxHttpResponse response();

    default ScxHttpHeader getHeader(String string) {
        return headers().get(string);
    }

    default ScxHttpPathQuery query() {
        return path().query();
    }

}
