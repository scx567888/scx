package cool.scx.http;

public interface ScxHttpRequest {

    ScxHttpMethod method();

    ScxHttpPath path();

    HttpVersion version();

    ScxHttpHeaders headers();

    ScxHttpBody body();

    ScxHttpResponse response();

}
