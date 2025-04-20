package cool.scx.data.field_policy;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.addAll;

/// 字段过滤器
///
/// @author scx567888
/// @version 0.0.1
public final class FieldPolicyImpl implements FieldPolicy {

    private final FilterMode filterMode;
    private final Set<String> fieldNames;
    private final Map<String, String> fieldExpressions;
    private final Map<String, Boolean> ignoreNulls;
    private boolean ignoreNull;

    public FieldPolicyImpl(FilterMode filterMode) {
        this.filterMode = filterMode;
        this.fieldNames = new HashSet<>();
        this.fieldExpressions = new LinkedHashMap<>();//保证顺序很重要
        this.ignoreNulls = new LinkedHashMap<>();
        this.ignoreNull = true;
    }

    @Override
    public FieldPolicy included(String... fieldNames) {
        return switch (filterMode) {
            case INCLUDED -> addFieldNames(fieldNames);
            case EXCLUDED -> removeFieldNames(fieldNames);
        };
    }

    @Override
    public FieldPolicy excluded(String... fieldNames) {
        return switch (filterMode) {
            case EXCLUDED -> addFieldNames(fieldNames);
            case INCLUDED -> removeFieldNames(fieldNames);
        };
    }

    @Override
    public FilterMode filterMode() {
        return filterMode;
    }

    @Override
    public String[] fieldNames() {
        return fieldNames.toArray(String[]::new);
    }

    @Override
    public FieldPolicy clearFieldNames() {
        fieldNames.clear();
        return this;
    }

    @Override
    public FieldPolicy ignoreNull(boolean ignoreNull) {
        this.ignoreNull = ignoreNull;
        return this;
    }

    @Override
    public boolean ignoreNull() {
        return ignoreNull;
    }

    @Override
    public FieldPolicy ignoreNull(String fieldName, boolean ignoreNull) {
        this.ignoreNulls.put(fieldName, ignoreNull);
        return this;
    }

    @Override
    public FieldPolicy removeIgnoreNull(String fieldName) {
        ignoreNulls.remove(fieldName);
        return this;
    }

    @Override
    public Map<String, Boolean> ignoreNulls() {
        return ignoreNulls;
    }

    @Override
    public FieldPolicy clearIgnoreNulls() {
        ignoreNulls.clear();
        return this;
    }

    @Override
    public FieldPolicy expression(String fieldName, String expression) {
        fieldExpressions.put(fieldName, expression);
        return this;
    }

    @Override
    public Map<String, String> expressions() {
        return fieldExpressions;
    }

    @Override
    public FieldPolicy removeExpression(String fieldName) {
        fieldExpressions.remove(fieldName);
        return this;
    }

    @Override
    public FieldPolicy clearExpressions() {
        fieldExpressions.clear();
        return this;
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
