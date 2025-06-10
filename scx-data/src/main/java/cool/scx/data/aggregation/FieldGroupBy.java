package cool.scx.data.aggregation;

/// FieldGroupBy
///
/// @author scx567888
/// @version 0.0.1
public final class FieldGroupBy extends GroupBy {

    private final String fieldName;

    public FieldGroupBy(String fieldName) {
        if (fieldName == null) {
            throw new NullPointerException("expression can not be null");
        }
        this.fieldName = fieldName;
    }

    public String fieldName() {
        return fieldName;
    }

}
