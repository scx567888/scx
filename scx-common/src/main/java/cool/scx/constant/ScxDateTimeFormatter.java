package cool.scx.constant;

import java.time.format.DateTimeFormatter;

/**
 * 所有常量
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class ScxDateTimeFormatter {

    /**
     * yyyy-MM-dd HH:mm:ss 精确到秒, 例 : "2023-05-09 13:02:35"
     */
    public static final DateTimeFormatter yyyy_MM_dd_HH_mm_ss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * yyyy-MM-dd HH:mm:ss.SSS 精确到毫秒, 例 : "2023-05-09 13:02:35.167"
     */
    public static final DateTimeFormatter yyyy_MM_dd_HH_mm_ss_SSS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * yyyy-MM-dd 精确到天, 例 : "2023-05-09"
     */
    public static final DateTimeFormatter yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * HH:mm:ss 精确到秒, 例 : "13:02:35"
     */
    public static final DateTimeFormatter HH_mm_ss = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * HH:mm:ss.SSS 精确到毫秒, 例 : "13:02:35.167"
     */
    public static final DateTimeFormatter HH_mm_ss_SSS = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

}
