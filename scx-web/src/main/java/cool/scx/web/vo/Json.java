package cool.scx.web.vo;

import cool.scx.web.routing.RoutingContext;

import static cool.scx.common.util.ObjectUtils.toJson;
import static cool.scx.web.ScxWebHelper.fillJsonContentType;

/**
 * Json 格式的返回值
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class Json implements BaseVo {

    private final Object data;

    private Json(Object data) {
        this.data = data;
    }

    public static Json of(Object data) {
        return new Json(data);
    }

    @Override
    public void accept(RoutingContext context) {
        fillJsonContentType(context.request().response()).send(toJson(data, ""));
    }

}
