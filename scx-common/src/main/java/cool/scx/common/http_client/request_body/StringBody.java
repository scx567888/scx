package cool.scx.common.http_client.request_body;

import cool.scx.common.http_client.ScxHttpClientRequestBody;

import static cool.scx.common.standard.HttpFieldName.CONTENT_TYPE;
import static cool.scx.common.standard.MediaType.TEXT_PLAIN;
import static java.net.http.HttpRequest.BodyPublisher;
import static java.net.http.HttpRequest.BodyPublishers.ofByteArray;
import static java.net.http.HttpRequest.Builder;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * StringBody
 */
public final class StringBody implements ScxHttpClientRequestBody {

    private final String bodyStr;

    public StringBody(String str) {
        bodyStr = str;
    }

    @Override
    public BodyPublisher bodyPublisher(Builder builder) {
        builder.setHeader(CONTENT_TYPE.toString(), TEXT_PLAIN.toString(UTF_8));
        return ofByteArray(bodyStr.getBytes(UTF_8));
    }

}
