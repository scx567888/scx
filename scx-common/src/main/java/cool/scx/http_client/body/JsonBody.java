package cool.scx.http_client.body;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.http_client.ScxHttpClientRequestBody;

import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.Builder;

import static cool.scx.util.ObjectUtils.toJson;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON;
import static java.net.http.HttpRequest.BodyPublishers.ofByteArray;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * a
 */
public final class JsonBody implements ScxHttpClientRequestBody {

    private final String bodyStr;

    public JsonBody(Object o) throws JsonProcessingException {
        bodyStr = toJson(o);
    }

    public JsonBody(String json) {
        bodyStr = json;
    }

    @Override
    public BodyPublisher bodyPublisher(Builder builder) {
        builder.setHeader(CONTENT_TYPE.toString(), APPLICATION_JSON + "; charset=utf-8");
        return ofByteArray(bodyStr.getBytes(UTF_8));
    }

}
