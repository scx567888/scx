package cool.scx.mvc.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.util.ObjectUtils;
import io.vertx.ext.web.RoutingContext;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Json 格式的返回值 (注意和 {@link Json} 区分, 此格式适合传递单个参数)
 *
 * @author scx567888
 * @version 1.9.5
 */
public abstract class Data implements BaseVo {

    /**
     * 操作成功
     *
     * @return json
     */
    public static DataOfMap ok() {
        return new DataOfMap("ok");
    }

    public static DataOfData ok(Object object) {
        return new DataOfData("ok", object);
    }

    /**
     * 操作失败
     *
     * @return json
     */
    public static DataOfData fail() {
        return new DataOfData("fail", null);
    }

    /**
     * 返回操作失败的 Json 带有消息
     *
     * @param failMessage 自定义的错误信息
     * @return json
     */
    public static DataOfData fail(String failMessage) {
        return new DataOfData(failMessage, null);
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
        return ObjectUtils.toJson(this.jsonBodyWrapper());
    }

    protected abstract JsonBodyWrapper<?> jsonBodyWrapper();

    /**
     * a
     *
     * @param defaultValue a
     * @return a
     */
    public String toJson(String defaultValue) {
        return ObjectUtils.toJson(this.jsonBodyWrapper(), defaultValue);
    }

    record JsonBodyWrapper<T>(String message, T data) {

    }

    public static class DataOfData extends Data {

        protected final JsonBodyWrapper<Object> jsonBodyWrapper;

        public DataOfData(String message, Object object) {
            this.jsonBodyWrapper = new JsonBodyWrapper<>(message, object);
        }

        @Override
        protected JsonBodyWrapper<?> jsonBodyWrapper() {
            return this.jsonBodyWrapper;
        }
    }

    public static class DataOfMap extends Data {

        protected final JsonBodyWrapper<Map<String, Object>> jsonBodyWrapper;

        private DataOfMap(String message) {
            this.jsonBodyWrapper = new JsonBodyWrapper<>(message, new LinkedHashMap<>());
        }

        /**
         * 设置操作返回的数据，数据使用自定义的key存储
         *
         * @param dataKey 自定义的key
         * @param dataVal 值
         * @return json
         */
        public DataOfMap put(String dataKey, Object dataVal) {
            jsonBodyWrapper.data.put(dataKey, dataVal);
            return this;
        }

        @Override
        protected JsonBodyWrapper<?> jsonBodyWrapper() {
            return jsonBodyWrapper;
        }

    }

}
