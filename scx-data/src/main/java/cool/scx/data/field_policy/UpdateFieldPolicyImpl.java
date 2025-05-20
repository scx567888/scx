package cool.scx.data.field_policy;

import java.util.LinkedHashMap;
import java.util.Map;

public class UpdateFieldPolicyImpl extends FieldPolicyImpl<UpdateFieldPolicy> implements UpdateFieldPolicy {

    private final Map<String, String> expressions;
    private final Map<String, Boolean> ignoreNulls;
    private boolean ignoreNull;

    public UpdateFieldPolicyImpl(FilterMode filterMode) {
        super(filterMode);
        this.expressions = new LinkedHashMap<>();//保证顺序很重要
        this.ignoreNulls = new LinkedHashMap<>();
        this.ignoreNull = true;
    }

    @Override
    public UpdateFieldPolicy ignoreNull(boolean ignoreNull) {
        this.ignoreNull = ignoreNull;
        return this;
    }

    @Override
    public UpdateFieldPolicy ignoreNull(String fieldName, boolean ignoreNull) {
        this.ignoreNulls.put(fieldName, ignoreNull);
        return this;
    }

    @Override
    public UpdateFieldPolicy expression(String fieldName, String expression) {
        this.expressions.put(fieldName, expression);
        return this;
    }

    @Override
    public boolean getIgnoreNull() {
        return ignoreNull;
    }

    @Override
    public Map<String, Boolean> getIgnoreNulls() {
        return ignoreNulls;
    }

    @Override
    public Map<String, String> getExpressions() {
        return expressions;
    }

    @Override
    public UpdateFieldPolicy clearIgnoreNulls() {
        ignoreNulls.clear();
        return this;
    }

    @Override
    public UpdateFieldPolicy clearExpressions() {
        expressions.clear();
        return this;
    }

    @Override
    public UpdateFieldPolicy removeIgnoreNull(String fieldName) {
        ignoreNulls.remove(fieldName);
        return this;
    }

    @Override
    public UpdateFieldPolicy removeExpression(String fieldName) {
        expressions.remove(fieldName);
        return this;
    }

}
