package cool.scx.web.vo;

import cool.scx.http.content_type.ContentType;
import cool.scx.http.routing.RoutingContext;

import static cool.scx.common.util.ObjectUtils.toXml;
import static cool.scx.http.MediaType.APPLICATION_XML;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Xml 格式的返回值
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class Xml implements BaseVo {

    private final Object data;

    private Xml(Object data) {
        this.data = data;
    }

    public static Xml of(Object data) {
        return new Xml(data);
    }

    @Override
    public void accept(RoutingContext context) {
        context.response()
                .contentType(ContentType.of(APPLICATION_XML).charset(UTF_8))
                .send(toXml(data, ""));
    }

}
