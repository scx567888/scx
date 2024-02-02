package cool.scx.http_client.body;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.http_client.ScxHttpClientRequestBody;

import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.Builder;

import static cool.scx.standard.HttpHeader.CONTENT_TYPE;
import static cool.scx.standard.MediaType.APPLICATION_XML;
import static cool.scx.util.ObjectUtils.toXml;
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
        builder.setHeader(CONTENT_TYPE.toString(), APPLICATION_XML.toString(UTF_8));
        return ofByteArray(bodyStr.getBytes(UTF_8));
    }

}
