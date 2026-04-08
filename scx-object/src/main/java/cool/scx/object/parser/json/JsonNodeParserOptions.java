package cool.scx.object.parser.json;

/// JsonNodeParserOptions
///
/// @author scx567888
/// @version 0.0.1
public final class JsonNodeParserOptions {

    // 重复字段策略
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
    // 最大数字长度
    private int maxNumberLength;

    public JsonNodeParserOptions() {
        this.duplicateFieldPolicy = DuplicateFieldPolicy.COVER;
        this.maxNestingDepth = 200; // 默认 200 既不会轻易栈溢出, 也足够 99.99% 的情况
        this.maxArraySize = 10 * 10000;
        this.maxFieldCount = 5000;
        this.maxStringLength = 2000 * 10000;
        this.maxFieldNameLength = 50000;
        this.maxNumberLength = 1000;
    }

    public DuplicateFieldPolicy duplicateFieldPolicy() {
        return duplicateFieldPolicy;
    }

    public JsonNodeParserOptions duplicateFieldPolicy(DuplicateFieldPolicy duplicateFieldPolicy) {
        if (duplicateFieldPolicy == null) {
            throw new NullPointerException("duplicateFieldPolicy cannot be null");
        }
        this.duplicateFieldPolicy = duplicateFieldPolicy;
        return this;
    }

    public int maxNestingDepth() {
        return maxNestingDepth;
    }

    public JsonNodeParserOptions maxNestingDepth(int maxNestingDepth) {
        if (maxNestingDepth < 0) {
            throw new IllegalArgumentException("maxNestingDepth cannot < 0");
        }
        this.maxNestingDepth = maxNestingDepth;
        return this;
    }

    public int maxArraySize() {
        return maxArraySize;
    }

    public JsonNodeParserOptions maxArraySize(int maxArraySize) {
        if (maxArraySize < 0) {
            throw new IllegalArgumentException("maxArraySize cannot < 0");
        }
        this.maxArraySize = maxArraySize;
        return this;
    }

    public int maxFieldCount() {
        return maxFieldCount;
    }

    public JsonNodeParserOptions maxFieldCount(int maxFieldCount) {
        if (maxFieldCount < 0) {
            throw new IllegalArgumentException("maxFieldCount cannot < 0");
        }
        this.maxFieldCount = maxFieldCount;
        return this;
    }

    public int maxStringLength() {
        return maxStringLength;
    }

    public JsonNodeParserOptions maxStringLength(int maxStringLength) {
        if (maxStringLength < 0) {
            throw new IllegalArgumentException("maxStringLength cannot < 0");
        }
        this.maxStringLength = maxStringLength;
        return this;
    }

    public int maxFieldNameLength() {
        return maxFieldNameLength;
    }

    public JsonNodeParserOptions maxFieldNameLength(int maxFieldNameLength) {
        if (maxFieldNameLength < 0) {
            throw new IllegalArgumentException("maxFieldNameLength cannot < 0");
        }
        this.maxFieldNameLength = maxFieldNameLength;
        return this;
    }

    public int maxNumberLength() {
        return maxNumberLength;
    }

    public JsonNodeParserOptions maxNumberLength(int maxNumberLength) {
        if (maxNumberLength < 0) {
            throw new IllegalArgumentException("maxNumberLength cannot < 0");
        }
        this.maxNumberLength = maxNumberLength;
        return this;
    }

}
