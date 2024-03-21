package cool.scx.mvc.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.standard.HttpHeader;
import cool.scx.common.standard.MediaType;
import cool.scx.common.util.ObjectUtils;
import io.vertx.ext.web.RoutingContext;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Json 格式的返回值 (注意和 {@link Json} 区分, 此格式适合传递单个参数)
 *
 * @author scx567888
 * @version 1.9.5
 */
public abstract class Result implements BaseVo {

    /**
     * 操作成功
     *
     * @return json
     */
    public static ResultOfMap ok() {
        return new ResultOfMap("ok");
    }

    public static ResultOfData ok(Object object) {
        return new ResultOfData("ok", object);
    }

    /**
     * 操作失败
     *
     * @return json
     */
    public static ResultOfMap fail() {
        return new ResultOfMap("fail");
    }

    /**
     * 返回操作失败的 Json 带有消息
     *
     * @param failMessage 自定义的错误信息
     * @return json
     */
    public static ResultOfMap fail(String failMessage) {
        return new ResultOfMap(failMessage);
    }

    /**
     * 返回操作失败的 Json 带有消息
     *
     * @param failMessage 自定义的错误信息
     * @param object      错误内容
     * @return json
     */
    public static ResultOfData fail(String failMessage, Object object) {
        return new ResultOfData(failMessage, object);
    }

    public static ResultOfMap of(String message) {
        return new ResultOfMap(message);
    }

    public static ResultOfData of(String message, Object object) {
        return new ResultOfData(message, object);
    }

    /**
     * {@inheritDoc}
     * <p>
     * sendToClient
     */
    @Override
    public void accept(RoutingContext context) {
        var accept = context.request().getHeader(HttpHeader.ACCEPT.toString());
        if (accept != null && accept.toLowerCase().startsWith(MediaType.APPLICATION_XML.toString())) {
            // 只有明确指定 接受参数是 application/xml 的才返回 xml
            BaseVo.fillXmlContentType(context.request().response()).end(toXml(""));
        } else { // 其余全部返回 json
            BaseVo.fillJsonContentType(context.request().response()).end(toJson(""));
        }
    }

    public abstract Body<?> body();

    /**
     * 将内部的 JsonBody 转换为 json 字符串
     *
     * @return r
     * @throws com.fasterxml.jackson.core.JsonProcessingException 转换失败
     */
    public String toJson() throws JsonProcessingException {
        return ObjectUtils.toJson(this.body());
    }

    public String toXml() throws JsonProcessingException {
        return ObjectUtils.toXml(this.body());
    }

    /**
     * a
     *
     * @param defaultValue a
     * @return a
     */
    public String toJson(String defaultValue) {
        return ObjectUtils.toJson(this.body(), defaultValue);
    }

    public String toXml(String defaultValue) {
        return ObjectUtils.toJson(this.body(), defaultValue);
    }

    public record Body<T>(String message, T data) {

    }

    public static class ResultOfData extends Result {

        protected final Body<Object> body;

        public ResultOfData(String message, Object object) {
            this.body = new Body<>(message, object);
        }

        @Override
        public Body<?> body() {
            return this.body;
        }
    }

    public static class ResultOfMap extends Result {

        protected final Body<Map<String, Object>> body;

        private ResultOfMap(String message) {
            this.body = new Body<>(message, new LinkedHashMap<>());
        }

        /**
         * 设置操作返回的数据，数据使用自定义的key存储
         *
         * @param dataKey 自定义的key
         * @param dataVal 值
         * @return json
         */
        public ResultOfMap put(String dataKey, Object dataVal) {
            body.data.put(dataKey, dataVal);
            return this;
        }

        public ResultOfMap remove(String dataKey, Object dataVal) {
            body.data.remove(dataKey, dataVal);
            return this;
        }

        @Override
        public Body<?> body() {
            return body;
        }

    }

}
