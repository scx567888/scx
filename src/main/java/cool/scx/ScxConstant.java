package cool.scx;

import java.time.format.DateTimeFormatter;

/**
 * 所有常量
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class ScxConstant {

    /**
     * SCX 版本号
     */
    public static final String SCX_VERSION = "1.13.0";

    /**
     * 默认 LocalDateTime 格式化类 注意!!! 所有前后台传输数据的序列化 , 数据库 Json 格式存储 均依赖此序列化格式
     */
    public static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

}
