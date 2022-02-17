package cool.scx;

import java.time.format.DateTimeFormatter;

/**
 * 所有常量
 */
public final class ScxConstant {

    /**
     * 默认 LocalDateTime 格式化类 注意!!! 所有前后台传输数据的序列化 , 数据库 Json 格式存储 均依赖此序列化格式
     */
    public static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final String HTTP_NOT_FOUND_TITLE = "Not Found !!!";

    public static final String HTTP_NO_PERM_TITLE = "No Perm !!!";

    public static final String HTTP_UNAUTHORIZED_TITLE = "Unauthorized !!!";

    public static final String HTTP_UNSUPPORTED_MEDIA_TYPE_TITLE = "Unsupported Media Type !!!";

    public static final String HTTP_BAD_REQUEST_TITLE = "Bad Request !!!";

    public static final String HTTP_INTERNAL_SERVER_ERROR_TITLE = "Internal Server Error !!!";

}
