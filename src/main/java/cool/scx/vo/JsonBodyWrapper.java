package cool.scx.vo;

/**
 * JsonBody
 */
public final class JsonBodyWrapper<T> {

    /**
     * 消息
     */
    public final String message;

    /**
     * 数据
     */
    public T data;

    /**
     * 构造函数
     *
     * @param message a
     * @param data    a
     */
    public JsonBodyWrapper(String message, T data) {
        this.message = message;
        this.data = data;
    }

}