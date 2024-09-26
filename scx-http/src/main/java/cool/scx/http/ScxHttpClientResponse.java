package cool.scx.http;

/**
 * ScxHttpClientResponse
 */
public interface ScxHttpClientResponse {

    HttpStatusCode status();

    ScxHttpClientResponseHeaders headers();

    ScxHttpBody body();

}
