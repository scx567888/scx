package cool.scx.object.mapping;

/// NodeMappingOptions
///
/// @author scx567888
/// @version 0.0.1
public final class FromNodeOptionsImpl implements FromNodeOptions {

    // 最大嵌套深度
    private int maxNestingDepth;

    public FromNodeOptionsImpl() {
        this.maxNestingDepth = 200;
    }

    public int maxNestingDepth() {
        return maxNestingDepth;
    }

    public FromNodeOptionsImpl maxNestingDepth(int maxNestingDepth) {
        if (maxNestingDepth < 0) {
            throw new IllegalArgumentException("maxNestingDepth cannot < 0");
        }
        this.maxNestingDepth = maxNestingDepth;
        return this;
    }

}
