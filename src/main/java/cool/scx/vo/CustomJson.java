package cool.scx.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.util.VoHelper;
import io.vertx.ext.web.RoutingContext;

/**
 * Json 格式的返回值
 *
 * @author scx567888
 * @version 1.9.5
 */
public final class CustomJson implements BaseVo {

    /**
     * 内部结构
     */
    private final JsonBodyWrapper<Object> jsonBodyWrapper;

    /**
     * 全参构造
     *
     * @param message 消息
     */
    private CustomJson(String message) {
        jsonBodyWrapper = new JsonBodyWrapper<>(message, null);
    }

    /**
     * 操作成功
     *
     * @return json
     */
    public static CustomJson ok() {
        return new CustomJson("ok");
    }

    /**
     * 操作失败
     *
     * @return json
     */
    public static CustomJson fail() {
        return new CustomJson("fail");
    }

    /**
     * 返回操作失败的 Json 带有消息
     *
     * @param failMessage 自定义的错误信息
     * @return json
     */
    public static CustomJson fail(String failMessage) {
        return new CustomJson(failMessage);
    }

    /**
     * 设置操作返回的数据，数据使用自定义的 Object 存储
     * <br>
     * 若重复调用则之前的会被覆盖
     *
     * @param dataVal 值
     * @return json
     */
    public CustomJson data(Object dataVal) {
        jsonBodyWrapper.data = dataVal;
        return this;
    }

    /**
     * {@inheritDoc}
     * <p>
     * sendToClient
     */
    @Override
    public void handle(RoutingContext context) {
        VoHelper.fillJsonContentType(context.request().response()).end(toJson(""));
    }

    /**
     * 将内部的 JsonBody 转换为 json 字符串
     *
     * @return r
     * @throws JsonProcessingException 转换失败
     */
    public String toJson() throws JsonProcessingException {
        return VoHelper.toJson(this.jsonBodyWrapper);
    }

    /**
     * a
     *
     * @param defaultValue a
     * @return a
     */
    public String toJson(String defaultValue) {
        return VoHelper.toJson(this.jsonBodyWrapper, defaultValue);
    }

}
