package cool.scx.enumeration;

/**
 * scx 特性枚举
 *
 * @author scx567888
 * @version 1.11.8
 */
public enum ScxFeature {

    /**
     * 显示 banner
     */
    SHOW_BANNER(true),

    /**
     * 显示 easyConfig 的信息
     */
    SHOW_EASY_CONFIG_INFO(true),

    /**
     * 是否使用开发人员错误页面
     */
    USE_DEVELOPMENT_ERROR_PAGE(false),

    /**
     * 目前框架中支持两种添加任务调度的方式 [注解 例如 ${@link org.springframework.scheduling.annotation.Scheduled}, 或使用 手动配置的方式 ${@link cool.scx.scheduler.ScxScheduler}]
     * <br>
     * 此标识表示是否扫描并启用 使用注解类型的定时任务 默认为 true , 及项目启动时扫描并启动所有的 注解类型定时任务
     * <br>
     * 若设置为 false 则相当与忽略整个项目中所有的 注解类型定时任务 (注意 !!! 手动设置的定时任务 不受此标识影响)
     */
    ENABLE_SCHEDULING_WITH_ANNOTATION(true);

    private final boolean _defaultValue;

    /**
     * <p>Constructor for ScxFeature.</p>
     *
     * @param defaultValue a boolean
     */
    ScxFeature(boolean defaultValue) {
        this._defaultValue = defaultValue;
    }

    /**
     * a
     *
     * @return a
     */
    public boolean defaultValue() {
        return _defaultValue;
    }

}
