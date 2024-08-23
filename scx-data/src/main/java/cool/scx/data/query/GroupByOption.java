package cool.scx.data.query;

/**
 * GroupByOption
 *
 * @author scx567888
 * @version 0.0.1
 */
public class GroupByOption {

    /**
     * 是否使用原始名称
     */
    private boolean useOriginalName = false;

    /**
     * 是否使用 json 类型的查询
     */
    private boolean useJsonExtract = false;

    public GroupByOption setUseOriginalName(boolean useOriginalName) {
        this.useOriginalName = useOriginalName;
        return this;
    }

    public GroupByOption setUseJsonExtract(boolean useJsonExtract) {
        this.useJsonExtract = useJsonExtract;
        return this;
    }

    public boolean useOriginalName() {
        return useOriginalName;
    }

    public boolean useJsonExtract() {
        return useJsonExtract;
    }

}
