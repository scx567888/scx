package cool.scx.object.parser.json;

/// 重复 field 策略
public enum DuplicateFieldPolicy {
    /// 覆盖
    COVER,
    /// 忽略
    IGNORE,
    /// 抛出异常
    THROW,
    /// 合并
    MERGE
}
