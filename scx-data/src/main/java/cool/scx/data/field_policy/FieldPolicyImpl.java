package cool.scx.data.field_policy;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.addAll;

/// 字段过滤器
///
/// @author scx567888
/// @version 0.0.1
public final class FieldPolicyImpl implements FieldPolicy {

    private final FilterMode filterMode;
    private final Set<String> fieldNames;
    private final Set<FieldExpression> fieldExpressions;
    private boolean ignoreNullValue;

    public FieldPolicyImpl(FilterMode filterMode) {
        this.filterMode = filterMode;
        this.fieldNames = new HashSet<>();
        this.fieldExpressions = new HashSet<>();
        this.ignoreNullValue = true;
    }

    @Override
    public FieldPolicy addIncluded(String... fieldNames) {
        return switch (filterMode) {
            case INCLUDED -> addFieldNames(fieldNames);
            case EXCLUDED -> removeFieldNames(fieldNames);
        };
    }

    @Override
    public FieldPolicy addExcluded(String... fieldNames) {
        return switch (filterMode) {
            case EXCLUDED -> addFieldNames(fieldNames);
            case INCLUDED -> removeFieldNames(fieldNames);
        };
    }

    @Override
    public FieldPolicy removeIncluded(String... fieldNames) {
        return addExcluded(fieldNames);
    }

    @Override
    public FieldPolicy removeExcluded(String... fieldNames) {
        return addIncluded(fieldNames);
    }

    @Override
    public FieldPolicy ignoreNullValue(boolean ignoreNullValue) {
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
    public FieldPolicy clear() {
        this.fieldNames.clear();
        return this;
    }

    @Override
    public FieldPolicy addFieldExpression(FieldExpression... fieldExpressions) {
        addAll(this.fieldExpressions, fieldExpressions);
        return this;
    }

    @Override
    public FieldExpression[] getFieldExpressions() {
        return fieldExpressions.toArray(FieldExpression[]::new);
    }

    public FieldPolicy addFieldNames(String... fieldNames) {
        addAll(this.fieldNames, fieldNames);
        return this;
    }

    public FieldPolicy removeFieldNames(String... fieldNames) {
        for (var fieldName : fieldNames) {
            this.fieldNames.remove(fieldName);
        }
        return this;
    }

}
