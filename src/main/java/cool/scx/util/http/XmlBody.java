package cool.scx.util.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.util.ObjectUtils;

import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;

/**
 * a
 */
public final class XmlBody implements HttpClientHelper.Body {

    private final String bodyStr;

    public XmlBody(Object o) throws JsonProcessingException {
        bodyStr = ObjectUtils.toXml(o);
    }

    @Override
    public HttpRequest.BodyPublisher getBodyPublisher(HttpRequest.Builder builder) {
        builder.setHeader("content-type", "application/xml;charset=utf-8");
        return HttpRequest.BodyPublishers.ofByteArray(bodyStr.getBytes(StandardCharsets.UTF_8));
    }

}
