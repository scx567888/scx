package cool.scx.object.mapper;

/// ToNodeOptionsImpl
///
/// @author scx567888
/// @version 0.0.1
public final class ToNodeOptionsImpl implements ToNodeOptions {

    // 忽略空值
    private boolean ignoreNullValue;
    // 空 key 
    private String nullKey;
    // 最大嵌套深度
    private int maxNestingDepth;

    public ToNodeOptionsImpl() {
        this.ignoreNullValue = false;
        this.nullKey = "";
        this.maxNestingDepth = 200;
    }

    @Override
    public String nullKey() {
        return nullKey;
    }

    public ToNodeOptionsImpl nullKey(String nullKey) {
        this.nullKey = nullKey;
        return this;
    }

    @Override
    public boolean ignoreNullValue() {
        return ignoreNullValue;
    }

    public ToNodeOptionsImpl ignoreNullValue(boolean ignoreNullValue) {
        this.ignoreNullValue = ignoreNullValue;
        return this;
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

}
