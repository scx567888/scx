package cool.scx.http_server;

public interface ScxHttpRequest {

    ScxHttpMethod method();

    ScxHttpPath path();

    ScxHttpVersion version();

    ScxHttpHeaders headers();

    ScxHttpBody body();

    ScxHttpResponse response();

    default String getHeader(String string) {
        return headers().get(string).value();
    }

    default ScxHttpPathQuery query() {
        return path().query();
    }

}
