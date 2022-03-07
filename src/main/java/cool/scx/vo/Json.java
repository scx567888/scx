package cool.scx.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Json 格式的返回值 (注意 {@link DataJson} 进行区分, 此格式适合传递多个参数)
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class Json implements BaseVo {

    /**
     * 内部结构
     */
    private final JsonBodyWrapper<Map<String, Object>> jsonBodyWrapper;

    /**
     * 全参构造
     *
     * @param message 消息
     */
    private Json(String message) {
        jsonBodyWrapper = new JsonBodyWrapper<>(message, new HashMap<>());
    }

    /**
     * 操作成功
     *
     * @return json
     */
    public static Json ok() {
        return new Json("ok");
    }

    /**
     * 操作失败
     *
     * @return json
     */
    public static Json fail() {
        return new Json("fail");
    }

    /**
     * 返回操作失败的 Json 带有消息
     *
     * @param failMessage 自定义的错误信息
     * @return json
     */
    public static Json fail(String failMessage) {
        return new Json(failMessage);
    }

    /**
     * 设置操作返回的数据，数据使用自定义的key存储
     *
     * @param dataKey 自定义的key
     * @param dataVal 值
     * @return json
     */
    public Json put(String dataKey, Object dataVal) {
        jsonBodyWrapper.data.put(dataKey, dataVal);
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
     * @throws com.fasterxml.jackson.core.JsonProcessingException 转换失败
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
