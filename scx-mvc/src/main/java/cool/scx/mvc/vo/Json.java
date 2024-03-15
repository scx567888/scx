package cool.scx.mvc.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.util.ObjectUtils;
import io.vertx.ext.web.RoutingContext;

/**
 * Json 格式的返回值
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class Json implements BaseVo {

    /**
     * 内部结构
     */
    private final Object data;

    /**
     * 全参构造
     *
     * @param data 消息
     */
    private Json(Object data) {
        this.data = data;
    }

    /**
     * a
     *
     * @param data a
     * @return a
     */
    public static Json of(Object data) {
        return new Json(data);
    }

    /**
     * {@inheritDoc}
     * <p>
     * sendToClient
     */
    @Override
    public void accept(RoutingContext context) {
        BaseVo.fillJsonContentType(context.request().response()).end(toJson(""));
    }

    /**
     * 将内部的 JsonBody 转换为 json 字符串
     *
     * @return r
     * @throws com.fasterxml.jackson.core.JsonProcessingException 转换失败
     */
    public String toJson() throws JsonProcessingException {
        return ObjectUtils.toJson(this.data);
    }

    /**
     * a
     *
     * @param defaultValue a
     * @return a
     */
    public String toJson(String defaultValue) {
        return ObjectUtils.toJson(this.data, defaultValue);
    }

}
