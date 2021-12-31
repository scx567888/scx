package cool.scx.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.vertx.ext.web.RoutingContext;

/**
 * Xml 格式的返回值
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class Xml implements BaseVo {

    /**
     * 内部结构
     */
    private final Object data;

    /**
     * 全参构造
     *
     * @param data 消息
     */
    public Xml(Object data) {
        this.data = data;
    }

    /**
     * {@inheritDoc}
     * <p>
     * sendToClient
     */
    @Override
    public void handle(RoutingContext context) {
        VoHelper.fillXmlContentType(context.request().response()).end(toXml(""));
    }

    /**
     * 将内部的 JsonBody 转换为 json 字符串
     *
     * @return r
     * @throws JsonProcessingException 转换失败
     */
    public String toXml() throws JsonProcessingException {
        return VoHelper.toXml(this.data);
    }

    /**
     * a
     *
     * @param defaultValue a
     * @return a
     */
    public String toXml(String defaultValue) {
        return VoHelper.toXml(this.data, defaultValue);
    }

}
