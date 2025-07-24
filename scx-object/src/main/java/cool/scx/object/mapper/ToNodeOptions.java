package cool.scx.object.mapper;

public interface ToNodeOptions {

    /// 是否忽略 null 值, 多用于 Map 和 Object
    boolean ignoreNullValue();

    /// null 时 对应的 key
    String nullKey();

}
