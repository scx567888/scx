package cool.scx.object.mapping.mapper.map;

import cool.scx.object.mapping.NodeMapperOptions;

public class MapNodeMapperOptions implements NodeMapperOptions {

    private String nullKey;
    private boolean ignoreNullValue;

    public MapNodeMapperOptions() {
        this.nullKey = "";
        this.ignoreNullValue = false;
    }

    /// null 时 对应的 key
    public String nullKey() {
        return nullKey;
    }

    public MapNodeMapperOptions nullKey(String nullKey) {
        if (nullKey == null) {
            throw new NullPointerException("nullKey cannot be null");
        }
        this.nullKey = nullKey;
        return this;
    }

    /// 是否忽略 null 值, 多用于 Map 和 Object
    public boolean ignoreNullValue() {
        return ignoreNullValue;
    }

    public MapNodeMapperOptions ignoreNullValue(boolean ignoreNullValue) {
        this.ignoreNullValue = ignoreNullValue;
        return this;
    }

}
