package cool.scx.data.aggregation;

public final class FieldGroupBy extends GroupBy {

    private final String fieldName;

    public FieldGroupBy(String fieldName) {
        //名称不能为空
        if (fieldName == null) {
            throw new NullPointerException("GroupBy 参数错误 : fieldName 不能为空 !!!");
        }
        this.fieldName = fieldName;
    }

    public String fieldName() {
        return fieldName;
    }

}
