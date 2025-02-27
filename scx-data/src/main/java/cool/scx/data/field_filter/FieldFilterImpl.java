package cool.scx.data.field_filter;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.addAll;

/// 字段过滤器
///
/// @author scx567888
/// @version 0.0.1
public final class FieldFilterImpl implements FieldFilter {

    private final FilterMode filterMode;
    private final Set<String> fieldNames;
    private boolean ignoreNullValue;

    public FieldFilterImpl(FilterMode filterMode) {
        this.filterMode = filterMode;
        this.fieldNames = new HashSet<>();
        this.ignoreNullValue = true;
    }

    @Override
    public FieldFilter addIncluded(String... fieldNames) {
        return switch (filterMode) {
            case INCLUDED -> addFieldNames(fieldNames);
            case EXCLUDED -> removeFieldNames(fieldNames);
        };
    }

    @Override
    public FieldFilter addExcluded(String... fieldNames) {
        return switch (filterMode) {
            case EXCLUDED -> addFieldNames(fieldNames);
            case INCLUDED -> removeFieldNames(fieldNames);
        };
    }

    @Override
    public FieldFilter removeIncluded(String... fieldNames) {
        return addExcluded(fieldNames);
    }

    @Override
    public FieldFilter removeExcluded(String... fieldNames) {
        return addIncluded(fieldNames);
    }

    @Override
    public FieldFilter ignoreNullValue(boolean ignoreNullValue) {
        this.ignoreNullValue = ignoreNullValue;
        return this;
    }

    @Override
    public FilterMode getFilterMode() {
        return filterMode;
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

    public FieldFilter addFieldNames(String... fieldNames) {
        addAll(this.fieldNames, fieldNames);
        return this;
    }

    public FieldFilter removeFieldNames(String... fieldNames) {
        for (var fieldName : fieldNames) {
            this.fieldNames.remove(fieldName);
        }
        return this;
    }

}
