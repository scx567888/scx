package cool.scx.http_client.body;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.http_client.ScxHttpClientRequestBody;

import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.Builder;

import static cool.scx.util.ObjectUtils.toXml;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_XML;
import static java.net.http.HttpRequest.BodyPublishers.ofByteArray;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * a
 */
public final class XmlBody implements ScxHttpClientRequestBody {

    private final String bodyStr;

    public XmlBody(Object o) throws JsonProcessingException {
        bodyStr = toXml(o);
    }

    public XmlBody(String xml) {
        bodyStr = xml;
    }

    @Override
    public BodyPublisher bodyPublisher(Builder builder) {
        builder.setHeader(CONTENT_TYPE.toString(), APPLICATION_XML + "; charset=utf-8");
        return ofByteArray(bodyStr.getBytes(UTF_8));
    }

}
