package cool.scx.object.mapping;

import java.util.HashMap;
import java.util.Map;

/// ToNodeOptionsImpl
///
/// @author scx567888
/// @version 0.0.1
public final class ToNodeOptionsImpl implements ToNodeOptions {

    // 忽略空值
    private Map<Class<?>, NodeMapperOptions> mapperOptionsMap;
    // 最大嵌套深度
    private int maxNestingDepth;

    public ToNodeOptionsImpl() {
        this.mapperOptionsMap = null;
        this.maxNestingDepth = 200;
    }

    public int maxNestingDepth() {
        return maxNestingDepth;
    }

    public ToNodeOptionsImpl maxNestingDepth(int maxNestingDepth) {
        if (maxNestingDepth < 0) {
            throw new IllegalArgumentException("maxNestingDepth cannot < 0");
        }
        this.maxNestingDepth = maxNestingDepth;
        return this;
    }

    @Override
    public ToNodeOptionsImpl addMapperOptions(NodeMapperOptions... optionsList) {
        if (mapperOptionsMap == null) {
            mapperOptionsMap = new HashMap<>();
        }
        for (var o : optionsList) {
            mapperOptionsMap.put(o.getClass(), o);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends NodeMapperOptions> T getMapperOptions(Class<T> mapNodeMapperOptionsClass) {
        if (mapperOptionsMap == null) {
            return null;
        }
        return (T) mapperOptionsMap.get(mapNodeMapperOptionsClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends NodeMapperOptions> T getMapperOptions(Class<T> mapNodeMapperOptionsClass, T defaultValue) {
        if (mapperOptionsMap == null) {
            return defaultValue;
        }
        return (T) mapperOptionsMap.getOrDefault(mapNodeMapperOptionsClass, defaultValue);
    }

}
