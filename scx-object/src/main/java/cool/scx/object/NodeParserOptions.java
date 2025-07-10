package cool.scx.object;

public class NodeParserOptions {

    private DuplicateFieldPolicy duplicateFieldPolicy;
    private int maxDepth;

    public NodeParserOptions() {
        this.duplicateFieldPolicy = DuplicateFieldPolicy.COVER;
        this.maxDepth = 100;
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
    
    public int maxDepth() {
        return maxDepth;
    }
    
    public NodeParserOptions maxDepth(int maxDepth) {
        if (maxDepth < 0) {
            throw new IllegalArgumentException("maxDepth is negative");
        }
        this.maxDepth = maxDepth;
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
