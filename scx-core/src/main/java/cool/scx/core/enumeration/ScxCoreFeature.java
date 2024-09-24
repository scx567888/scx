package cool.scx.core.enumeration;

import cool.scx.scheduling.ScxScheduler;
import cool.scx.config.ScxFeature;

/**
 * scx 特性枚举
 *
 * @author scx567888
 * @version 1.11.8
 */
public enum ScxCoreFeature implements ScxFeature<Boolean> {

    /**
     * 显示 banner
     */
    SHOW_BANNER(true),

    /**
     * 显示 coreConfig 的信息
     */
    SHOW_OPTIONS_INFO(true),

    /**
     * 显示模块生命周期信息
     */
    SHOW_MODULE_LIFE_CYCLE_INFO(true),

    /**
     * 显示启动信息
     */
    SHOW_START_UP_INFO(true),

    /**
     * 是否使用开发人员错误页面
     */
    USE_DEVELOPMENT_ERROR_PAGE(false),

    /**
     * 目前框架中支持两种添加任务调度的方式 [注解 例如 ${@link org.springframework.scheduling.annotation.Scheduled}, 或使用 手动配置的方式 ${@link ScxScheduler}]
     * <br>
     * 此标识表示是否扫描并启用 使用注解类型的定时任务 默认为 true , 及项目启动时扫描并启动所有的 注解类型定时任务
     * <br>
     * 若设置为 false 则相当与忽略整个项目中所有的 注解类型定时任务 (注意 !!! 手动设置的定时任务 不受此标识影响)
     */
    ENABLE_SCHEDULING_WITH_ANNOTATION(true),

    /**
     * 是否允许 bean 之间的循环依赖 默认 false
     */
    ALLOW_CIRCULAR_REFERENCES(false),

    /**
     * 是否使用 SPY 进行 SQL 监控 (注意 !!! 开启会影响性能)
     */
    USE_SPY(false);

    /**
     * a
     */
    private final boolean _defaultValue;

    /**
     * <p>Constructor for ScxFeature.</p>
     *
     * @param defaultValue a boolean
     */
    ScxCoreFeature(boolean defaultValue) {
        this._defaultValue = defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean defaultValue() {
        return _defaultValue;
    }

}
