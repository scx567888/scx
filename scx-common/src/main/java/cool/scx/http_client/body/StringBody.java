package cool.scx.http_client.body;

import cool.scx.http_client.ScxHttpClientRequestBody;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;
import static java.net.http.HttpRequest.BodyPublisher;
import static java.net.http.HttpRequest.BodyPublishers.ofByteArray;
import static java.net.http.HttpRequest.Builder;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * a
 */
public final class StringBody implements ScxHttpClientRequestBody {

    private final String bodyStr;

    public StringBody(String str) {
        bodyStr = str;
    }

    @Override
    public BodyPublisher bodyPublisher(Builder builder) {
        builder.setHeader(CONTENT_TYPE.toString(), TEXT_PLAIN + "; charset=utf-8");
        return ofByteArray(bodyStr.getBytes(UTF_8));
    }

}
