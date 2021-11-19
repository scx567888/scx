package cool.scx.config;

import cool.scx.enumeration.ScxFeature;

import java.util.EnumMap;
import java.util.Map;

/**
 * a
 */
public final class ScxFeatureConfig {

    /**
     * Scx 特性列表
     */
    private final Map<ScxFeature, Boolean> SCX_APP_FEATURE_STATE_MAP = new EnumMap<>(ScxFeature.class);

    /**
     * 设置值
     *
     * @param scxFeature s
     * @param state      s
     * @return a
     */
    public ScxFeatureConfig configure(ScxFeature scxFeature, boolean state) {
        SCX_APP_FEATURE_STATE_MAP.put(scxFeature, state);
        return this;
    }

    /**
     * 获取特性的值 如果没有显式设置 则返回默认值
     *
     * @param scxFeature s
     * @return s
     */
    public boolean getFeatureState(ScxFeature scxFeature) {
        return SCX_APP_FEATURE_STATE_MAP.getOrDefault(scxFeature, scxFeature.defaultValue());
    }

}
