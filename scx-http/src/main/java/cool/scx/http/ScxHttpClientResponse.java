package cool.scx.http;

public interface ScxHttpClientResponse {

    HttpStatusCode statusCode();

    ScxHttpHeaders headers();

    ScxHttpBody body();

}
