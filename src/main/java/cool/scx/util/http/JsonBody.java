package cool.scx.util.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.util.ObjectUtils;
import cool.scx.util.http.HttpClientHelper;

import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;

/**
 * a
 */
public final class JsonBody implements HttpClientHelper.Body {

    private final String bodyStr;

    public JsonBody(Object o) throws JsonProcessingException {
        bodyStr = ObjectUtils.toJson(o);
    }

    @Override
    public HttpRequest.BodyPublisher getBodyPublisher(HttpRequest.Builder builder) {
        builder.setHeader("content-type", "application/json;charset=utf-8");
        return HttpRequest.BodyPublishers.ofByteArray(bodyStr.getBytes(StandardCharsets.UTF_8));
    }

}
