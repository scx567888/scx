package cool.scx.util.http;

import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;

public final class StringBody implements Body {

    private final String bodyStr;

    public StringBody(String str) {
        bodyStr = str;
    }

    @Override
    public HttpRequest.BodyPublisher getBodyPublisher(HttpRequest.Builder builder) {
        builder.setHeader("content-type", "text/plain;charset=utf-8");
        return HttpRequest.BodyPublishers.ofByteArray(bodyStr.getBytes(StandardCharsets.UTF_8));
    }

}
