package cool.scx.web.vo;

import cool.scx.http.content_type.ContentType;
import cool.scx.http.routing.RoutingContext;

import static cool.scx.common.util.ObjectUtils.toJson;
import static cool.scx.http.MediaType.APPLICATION_JSON;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Json 格式的返回值
 *
 * @author scx567888
 * @version 0.0.1
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
        context.request().response()
                .contentType(ContentType.of(APPLICATION_JSON).charset(UTF_8))
                .send(toJson(data, ""));
    }

}
