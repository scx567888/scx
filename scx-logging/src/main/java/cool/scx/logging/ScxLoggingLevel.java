package cool.scx.logging;

/**
 * ScxLoggingLevel 日志级别
 *
 * @author scx567888
 * @version 0.0.1
 */
public enum ScxLoggingLevel {

    OFF(0), FATAL(10), ERROR(20), WARN(30), INFO(40), DEBUG(50), TRACE(60), ALL(70);

    /**
     * int 级别
     */
    private final int intLevel;

    /**
     * 定长名称 (5位), 不足的补齐空格 方便打印时更好的格式化
     */
    private final String fixedLengthName;

    /**
     * a
     *
     * @param intLevel a
     */
    ScxLoggingLevel(int intLevel) {
        this.intLevel = intLevel;
        this.fixedLengthName = (name() + "  ").substring(0, 5);
    }

    /**
     * <p>intLevel.</p>
     *
     * @return a int
     */
    public int intLevel() {
        return intLevel;
    }

    /**
     * <p>fixedLengthName.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String fixedLengthName() {
        return fixedLengthName;
    }

}
