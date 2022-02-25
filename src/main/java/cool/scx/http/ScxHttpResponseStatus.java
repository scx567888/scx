package cool.scx.http;

/**
 * 规定一些常见的 http 相应状态
 */
public enum ScxHttpResponseStatus {

    BAD_REQUEST(400, "Bad Request !!!"), // 错误请求 一般为参数错误
    UNAUTHORIZED(401, "Unauthorized !!!"), // 未认证
    NO_PERM(403, "No Perm !!!"), // 没权限
    NOT_FOUND(404, "Not Found !!!"),  // 没找到
    METHOD_NOT_ALLOWED(405, "Method Not Allowed !!!"), //方法不被允许
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type !!!"), //不支持的 类型
    INTERNAL_SERVER_ERROR(500, "Internal Server Error !!!"); //服务器内部错误

    private final int statusCode;
    private final String reasonPhrase;

    ScxHttpResponseStatus(int statusCode, String reasonPhrase) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    public static ScxHttpResponseStatus findByStatusCode(int statusCode) {
        for (var s : ScxHttpResponseStatus.values()) {
            if (statusCode == s.statusCode) {
                return s;
            }
        }
        return null;
    }

    public int statusCode() {
        return statusCode;
    }

    public String reasonPhrase() {
        return reasonPhrase;
    }

}
