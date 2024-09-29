package cool.scx.web;

import cool.scx.http.ScxHttpClient;
import cool.scx.http.ScxHttpClientResponse;
import cool.scx.http.helidon.HelidonHttpClient;
import cool.scx.http.media.multi_part.MultiPart;
import cool.scx.http.uri.ScxURIWritable;

import static cool.scx.http.HttpMethod.POST;

public class ScxHttpClientHelper {

    public static final ScxHttpClient DEFAULT_HTTP_CLIENT = new HelidonHttpClient();

    public static ScxHttpClientResponse post(ScxURIWritable uri, MultiPart content) {
        return DEFAULT_HTTP_CLIENT.request().method(POST).uri(uri).send(content);
    }


}
