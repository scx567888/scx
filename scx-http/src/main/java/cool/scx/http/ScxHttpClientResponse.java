package cool.scx.http;

/**
 * ScxHttpClientResponse
 */
public interface ScxHttpClientResponse {

    HttpStatusCode status();

    ScxHttpHeaders headers();

    ScxHttpBody body();

}
