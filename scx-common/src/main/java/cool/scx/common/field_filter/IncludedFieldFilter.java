package cool.scx.common.field_filter;

import java.util.HashSet;
import java.util.Set;

import static cool.scx.common.field_filter.FilterMode.INCLUDED;
import static java.util.Collections.addAll;

/**
 * 列过滤器
 *
 * @author scx567888
 * @version 0.1.3
 */
public class IncludedFieldFilter implements FieldFilter {

    private final Set<String> fieldNames;
    private boolean ignoreNullValue;

    public IncludedFieldFilter() {
        this.fieldNames = new HashSet<>();
        this.ignoreNullValue = true;
    }

    @Override
    public FieldFilter addIncluded(String... fieldNames) {
        return _addFieldNames(fieldNames);
    }

    @Override
    public FieldFilter addExcluded(String... fieldNames) {
        return _removeFieldNames(fieldNames);
    }

    @Override
    public FieldFilter removeIncluded(String... fieldNames) {
        return _addFieldNames(fieldNames);
    }

    @Override
    public FieldFilter removeExcluded(String... fieldNames) {
        return _removeFieldNames(fieldNames);
    }

    @Override
    public FieldFilter ignoreNullValue(boolean ignoreNullValue) {
        this.ignoreNullValue = ignoreNullValue;
        return this;
    }

    @Override
    public FilterMode getFilterMode() {
        return INCLUDED;
    }

    @Override
    public String[] getFieldNames() {
        return fieldNames.toArray(String[]::new);
    }

    @Override
    public boolean getIgnoreNullValue() {
        return ignoreNullValue;
    }

    @Override
    public FieldFilter clear() {
        this.fieldNames.clear();
        return this;
    }

    private FieldFilter _addFieldNames(String... fieldNames) {
        addAll(this.fieldNames, fieldNames);
        return this;
    }

    private FieldFilter _removeFieldNames(String... fieldNames) {
        for (var fieldName : fieldNames) {
            this.fieldNames.remove(fieldName);
        }
        return this;
    }

}
