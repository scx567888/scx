package cool.scx.object.mapping.mapper.record;

import cool.scx.object.mapping.NodeMapperOptions;

public class RecordNodeMapperOptions implements NodeMapperOptions {

    private boolean ignoreNullValue;

    public RecordNodeMapperOptions() {
        this.ignoreNullValue = false;
    }

    /// 是否忽略 null 值, 多用于 Map 和 Object
    public boolean ignoreNullValue() {
        return ignoreNullValue;
    }

    public RecordNodeMapperOptions ignoreNullValue(boolean ignoreNullValue) {
        this.ignoreNullValue = ignoreNullValue;
        return this;
    }

}
