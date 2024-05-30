package cool.scx.common.http_client.request_body;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.http_client.ScxHttpClientRequestBody;

import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.Builder;

import static cool.scx.common.standard.HttpFieldName.CONTENT_TYPE;
import static cool.scx.common.standard.MediaType.APPLICATION_XML;
import static cool.scx.common.util.ObjectUtils.toXml;
import static java.net.http.HttpRequest.BodyPublishers.ofByteArray;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * XmlBody
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
