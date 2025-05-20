package cool.scx.data.field_policy;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.addAll;

/// 字段过滤器
///
/// @author scx567888
/// @version 0.0.1
@SuppressWarnings("unchecked")
class FieldPolicyImpl<T extends FieldPolicy<T>> implements FieldPolicy<T> {

    private final FilterMode filterMode;
    private final Set<String> fieldNames;

    public FieldPolicyImpl(FilterMode filterMode) {
        this.filterMode = filterMode;
        this.fieldNames = new HashSet<>();
    }

    @Override
    public T include(String... fieldNames) {
        return switch (filterMode) {
            case INCLUDED -> addFieldNames(fieldNames);
            case EXCLUDED -> removeFieldNames(fieldNames);
        };
    }

    @Override
    public T exclude(String... fieldNames) {
        return switch (filterMode) {
            case EXCLUDED -> addFieldNames(fieldNames);
            case INCLUDED -> removeFieldNames(fieldNames);
        };
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
    public T clearFieldNames() {
        fieldNames.clear();
        return (T) this;
    }

    public T addFieldNames(String... fieldNames) {
        addAll(this.fieldNames, fieldNames);
        return (T) this;
    }

    public T removeFieldNames(String... fieldNames) {
        for (var fieldName : fieldNames) {
            this.fieldNames.remove(fieldName);
        }
        return (T) this;
    }

}
