package cool.scx.http.helidon;

import cool.scx.http.ScxHttpClient;
import cool.scx.http.ScxHttpClientResponse;
import cool.scx.http.media.multi_part.MultiPart;
import cool.scx.http.uri.ScxURIWritable;

import static cool.scx.http.HttpMethod.*;

public class ScxHttpClientHelper {

    public static final ScxHttpClient DEFAULT_HTTP_CLIENT = new HelidonHttpClient();

    public static ScxHttpClientResponse get(ScxURIWritable uri) {
        return DEFAULT_HTTP_CLIENT.request().method(GET).uri(uri).send();
    }

    public static ScxHttpClientResponse post(ScxURIWritable uri, MultiPart content) {
        return DEFAULT_HTTP_CLIENT.request().method(POST).uri(uri).send(content);
    }

    public static ScxHttpClientResponse put(ScxURIWritable uri, MultiPart content) {
        return DEFAULT_HTTP_CLIENT.request().method(PUT).uri(uri).send(content);
    }

    public static ScxHttpClientResponse delete(ScxURIWritable uri, MultiPart content) {
        return DEFAULT_HTTP_CLIENT.request().method(DELETE).uri(uri).send(content);
    }

    public static ScxHttpClientResponse get(String uri) {
        return DEFAULT_HTTP_CLIENT.request().method(GET).uri(uri).send();
    }

    public static ScxHttpClientResponse post(String uri, MultiPart content) {
        return DEFAULT_HTTP_CLIENT.request().method(POST).uri(uri).send(content);
    }

    public static ScxHttpClientResponse put(String uri, MultiPart content) {
        return DEFAULT_HTTP_CLIENT.request().method(PUT).uri(uri).send(content);
    }

    public static ScxHttpClientResponse delete(String uri, MultiPart content) {
        return DEFAULT_HTTP_CLIENT.request().method(DELETE).uri(uri).send(content);
    }

}
