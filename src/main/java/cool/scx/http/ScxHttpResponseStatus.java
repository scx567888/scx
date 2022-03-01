package cool.scx.http;

/**
 * 规定一些常见的 http 相应状态
 *
 * @author scx567888
 * @version 1.11.8
 */
public enum ScxHttpResponseStatus {

    /**
     * 错误请求 一般为参数错误
     */
    BAD_REQUEST(400, "Bad Request !!!"),

    /**
     * 未认证
     */
    UNAUTHORIZED(401, "Unauthorized !!!"),

    /**
     * 没权限
     */
    NO_PERM(403, "No Perm !!!"),

    /**
     * 没找到
     */
    NOT_FOUND(404, "Not Found !!!"),

    /**
     * 方法不被允许
     */
    METHOD_NOT_ALLOWED(405, "Method Not Allowed !!!"),

    /**
     * 不支持的 类型
     */
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type !!!"),

    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500, "Internal Server Error !!!");

    /**
     * a
     */
    private final int statusCode;

    /**
     * a
     */
    private final String reasonPhrase;

    /**
     * <p>Constructor for ScxHttpResponseStatus.</p>
     *
     * @param statusCode   a int
     * @param reasonPhrase a {@link java.lang.String} object
     */
    ScxHttpResponseStatus(int statusCode, String reasonPhrase) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    /**
     * a
     *
     * @param statusCode a
     * @return a
     */
    public static ScxHttpResponseStatus findByStatusCode(int statusCode) {
        for (var s : ScxHttpResponseStatus.values()) {
            if (statusCode == s.statusCode) {
                return s;
            }
        }
        return null;
    }

    /**
     * a
     *
     * @return a
     */
    public int statusCode() {
        return statusCode;
    }

    /**
     * a
     *
     * @return a
     */
    public String reasonPhrase() {
        return reasonPhrase;
    }

}
