package cool.scx.web.vo;

import cool.scx.http.media_type.ScxMediaType;
import cool.scx.http.routing.RoutingContext;

import static cool.scx.http.media_type.MediaType.APPLICATION_XML;
import static java.nio.charset.StandardCharsets.UTF_8;

/// Xml 格式的返回值
///
/// @author scx567888
/// @version 0.0.1
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
                .contentType(ScxMediaType.of(APPLICATION_XML).charset(UTF_8))
                .send(data);
    }

}
