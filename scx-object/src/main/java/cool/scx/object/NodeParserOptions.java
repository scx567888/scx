package cool.scx.object;

public class NodeParserOptions {

    private DuplicateFieldPolicy duplicateFieldPolicy;

    public NodeParserOptions() {
        this.duplicateFieldPolicy = DuplicateFieldPolicy.COVER;
    }

    public DuplicateFieldPolicy duplicateFieldPolicy() {
        return duplicateFieldPolicy;
    }

    public NodeParserOptions duplicateFieldPolicy(DuplicateFieldPolicy duplicateFieldPolicy) {
        if (duplicateFieldPolicy == null) {
            throw new NullPointerException("duplicateFieldPolicy is null");
        }
        this.duplicateFieldPolicy = duplicateFieldPolicy;
        return this;
    }

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

}
