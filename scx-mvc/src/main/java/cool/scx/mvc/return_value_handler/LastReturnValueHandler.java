package cool.scx.mvc.return_value_handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.standard.HttpHeader;
import cool.scx.common.standard.MediaType;
import cool.scx.common.util.ObjectUtils;
import cool.scx.mvc.ScxMvcReturnValueHandler;
import cool.scx.mvc.vo.BaseVo;
import io.vertx.ext.web.RoutingContext;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class LastReturnValueHandler implements ScxMvcReturnValueHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHandle(Object result) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(Object result, RoutingContext context) throws JsonProcessingException {
        var accept = context.request().getHeader(HttpHeader.ACCEPT.toString());
        if (accept != null && accept.toLowerCase().startsWith(MediaType.APPLICATION_XML.toString())) {
            // 只有明确指定 接受参数是 application/xml 的才返回 xml
            BaseVo.fillXmlContentType(context.request().response()).end(ObjectUtils.toXml(result));
        } else { // 其余全部返回 json
            BaseVo.fillJsonContentType(context.request().response()).end(ObjectUtils.toJson(result));
        }
    }

}
