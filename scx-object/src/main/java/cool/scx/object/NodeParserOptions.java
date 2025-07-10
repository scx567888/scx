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
    // 最大字符串长度
    private int maxStringLength;
    // 最大字段名称长度
    private int maxFieldNameLength;

    public NodeParserOptions() {
        this.duplicateFieldPolicy = DuplicateFieldPolicy.COVER;
        this.maxNestingDepth = 100;
        this.maxArraySize = 20000;
        this.maxFieldCount = 5000;
        this.maxStringLength = 2000 * 10000;// 2千万
        this.maxFieldNameLength = 50000;
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

    public int maxStringLength() {
        return maxStringLength;
    }

    public NodeParserOptions maxStringLength(int maxStringLength) {
        if (maxStringLength < 0) {
            throw new IllegalArgumentException("maxStringLength cannot < 0");
        }
        this.maxStringLength = maxStringLength;
        return this;
    }

    public int maxFieldNameLength() {
        return maxFieldNameLength;
    }

    public NodeParserOptions maxFieldNameLength(int maxFieldNameLength) {
        if (maxFieldNameLength < 0) {
            throw new IllegalArgumentException("maxFieldNameLength cannot < 0");
        }
        this.maxFieldNameLength = maxFieldNameLength;
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
