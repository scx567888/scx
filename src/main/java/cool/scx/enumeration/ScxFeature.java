package cool.scx.enumeration;

/**
 * scx 特性枚举
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
    USE_DEVELOPMENT_ERROR_PAGE(false);

    private final boolean _defaultValue;

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
