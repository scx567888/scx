package cool.scx.common.field_filter;

import static cool.scx.common.field_filter.FilterMode.EXCLUDED;

/**
 * 列过滤器
 *
 * @author scx567888
 * @version 0.1.3
 */
public class ExcludedFieldFilter extends AbstractFieldFilter {

    @Override
    public FieldFilter addIncluded(String... fieldNames) {
        return _removeFieldNames(fieldNames);
    }

    @Override
    public FieldFilter addExcluded(String... fieldNames) {
        return _addFieldNames(fieldNames);
    }

    @Override
    public FieldFilter removeIncluded(String... fieldNames) {
        return _removeFieldNames(fieldNames);
    }

    @Override
    public FieldFilter removeExcluded(String... fieldNames) {
        return _addFieldNames(fieldNames);
    }

    @Override
    public FilterMode getFilterMode() {
        return EXCLUDED;
    }

}
