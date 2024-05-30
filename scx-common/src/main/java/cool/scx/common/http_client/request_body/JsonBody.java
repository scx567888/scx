package cool.scx.common.http_client.request_body;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.http_client.ScxHttpClientRequestBody;

import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.Builder;

import static cool.scx.common.standard.HttpFieldName.CONTENT_TYPE;
import static cool.scx.common.standard.MediaType.APPLICATION_JSON;
import static cool.scx.common.util.ObjectUtils.toJson;
import static java.net.http.HttpRequest.BodyPublishers.ofByteArray;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * JsonBody
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
        builder.setHeader(CONTENT_TYPE.toString(), APPLICATION_JSON.toString(UTF_8));
        return ofByteArray(bodyStr.getBytes(UTF_8));
    }

}
