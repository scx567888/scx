package cool.scx.web.vo;

import io.vertx.ext.web.RoutingContext;

import static cool.scx.common.util.ObjectUtils.toXml;
import static cool.scx.web.ScxWebHelper.fillXmlContentType;

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
        fillXmlContentType(context.request().response()).end(toXml(data, ""));
    }

}
