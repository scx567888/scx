package cool.scx.data.query;

/**
 * OrderByOption
 *
 * @author scx567888
 * @version 0.0.1
 */
public class OrderByOption {

    /**
     * 是否替换已有的相同名称的 WhereBody
     */
    private boolean replace = false;

    /**
     * 是否使用原始名称
     */
    private boolean useOriginalName = false;

    /**
     * 是否使用 json 类型的查询
     */
    private boolean useJsonExtract = false;

    public OrderByOption setReplace(boolean replace) {
        this.replace = replace;
        return this;
    }

    public OrderByOption setUseOriginalName(boolean useOriginalName) {
        this.useOriginalName = useOriginalName;
        return this;
    }

    public OrderByOption setUseJsonExtract(boolean useJsonExtract) {
        this.useJsonExtract = useJsonExtract;
        return this;
    }

    public boolean replace() {
        return replace;
    }

    public boolean useOriginalName() {
        return useOriginalName;
    }

    public boolean useJsonExtract() {
        return useJsonExtract;
    }
    
}
