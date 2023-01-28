package cool.scx.constant;

import java.time.format.DateTimeFormatter;

/**
 * 所有常量
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class ScxConstant {

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final DateTimeFormatter NORMAL_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * yyyy-MM-dd HH:mm:ss.SSS
     */
    public static final DateTimeFormatter NORMAL_DATE_TIME_MS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * yyyy-MM-dd
     */
    public static final DateTimeFormatter NORMAL_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * HH:mm:ss
     */
    public static final DateTimeFormatter NORMAL_TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * HH:mm:ss.SSS
     */
    public static final DateTimeFormatter NORMAL_TIME_MS = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

}
