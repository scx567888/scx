package cool.scx.http.exception;

/**
 * 在 ScxMapping 注解标记的方法中抛出此异常会被ScxMappingHandler 进行截获并进行处理
 * <p>
 * 当我们的代码中有需要向客户端返回错误信息的时候
 * <p>
 * 推荐创建 HttpRequestException 的实现类并抛出异常 , 而不是手动进行异常的处理与响应的返回
 *
 * @author scx567888
 * @version 1.0.10
 */
public class ScxHttpException extends RuntimeException {

    /**
     * http 状态码
     */
    final int statusCode;

    /**
     * 简短说明
     */
    final String title;

    /**
     * a
     *
     * @param statusCode a
     * @param title      a
     */
    public ScxHttpException(int statusCode, String title) {
        super();
        this.statusCode = statusCode;
        this.title = title;
    }

    /**
     * a
     *
     * @param statusCode a
     * @param title      a
     * @param info       a
     */
    public ScxHttpException(int statusCode, String title, String info) {
        super(info);
        this.statusCode = statusCode;
        this.title = title;
    }

    /**
     * a
     *
     * @param statusCode a
     * @param title      a
     * @param throwable  a
     */
    public ScxHttpException(int statusCode, String title, Throwable throwable) {
        super(throwable);
        this.statusCode = statusCode;
        this.title = title;
    }

    /**
     * a
     *
     * @param statusCode a
     * @param title      a
     * @param info       a
     * @param throwable  a
     */
    public ScxHttpException(int statusCode, String title, String info, Throwable throwable) {
        super(info, throwable);
        this.statusCode = statusCode;
        this.title = title;
    }

    /**
     * a
     *
     * @return a
     */
    public final int statusCode() {
        return this.statusCode;
    }

    /**
     * a
     *
     * @return a
     */
    public final String title() {
        return this.title;
    }

}
