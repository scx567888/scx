package cool.scx.object;

public class NodeParserOptions {

    //重复字段策略
    private DuplicateFieldPolicy duplicateFieldPolicy;
    // 最大嵌套深度
    private int maxNestingDepth;
    // 最大数组长度
    private int maxArraySize;
    // 最大字段数量
    private int maxFieldCount;

    public NodeParserOptions() {
        this.duplicateFieldPolicy = DuplicateFieldPolicy.COVER;
        this.maxNestingDepth = 100;
        this.maxArraySize = 20000;
        this.maxFieldCount = 5000;
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

    public int maxNestingDepth() {
        return maxNestingDepth;
    }

    public NodeParserOptions maxNestingDepth(int maxNestingDepth) {
        if (maxNestingDepth < 0) {
            throw new IllegalArgumentException("maxNestingDepth cannot < 0");
        }
        this.maxNestingDepth = maxNestingDepth;
        return this;
    }

    public int maxArraySize() {
        return maxArraySize;
    }

    public NodeParserOptions maxArraySize(int maxArraySize) {
        if (maxArraySize < 0) {
            throw new IllegalArgumentException("maxArraySize cannot < 0");
        }
        this.maxArraySize = maxArraySize;
        return this;
    }

    public int maxFieldCount() {
        return maxFieldCount;
    }

    public NodeParserOptions maxFieldCount(int maxFieldCount) {
        if (maxFieldCount < 0) {
            throw new IllegalArgumentException("maxFieldCount cannot < 0");
        }
        this.maxFieldCount = maxFieldCount;
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
