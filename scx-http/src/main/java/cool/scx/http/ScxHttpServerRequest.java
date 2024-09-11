package cool.scx.http;

public interface ScxHttpServerRequest {

    ScxHttpMethod method();

    ScxHttpPath path();

    HttpVersion version();

    ScxHttpHeaders headers();

    ScxHttpBody body();

    ScxHttpServerResponse response();

    default String getHeader(ScxHttpHeaderName name) {
        return headers().get(name);
    }

}
