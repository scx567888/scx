package cool.scx.data.field_policy;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UpdateFieldPolicyImpl extends FieldPolicyImpl<UpdateFieldPolicy> implements UpdateFieldPolicy {

    private final Map<String, Boolean> ignoreNulls;
    private List<Expression> expressions;
    private boolean ignoreNull;

    public UpdateFieldPolicyImpl(FilterMode filterMode) {
        super(filterMode);
        this.expressions = new ArrayList<>();//保证顺序很重要
        this.ignoreNulls = new LinkedHashMap<>();
        this.ignoreNull = true;
    }

    @Override
    public UpdateFieldPolicyImpl ignoreNull(boolean ignoreNull) {
        this.ignoreNull = ignoreNull;
        return this;
    }

    @Override
    public UpdateFieldPolicy ignoreNull(String fieldName, boolean ignoreNull) {
        this.ignoreNulls.put(fieldName, ignoreNull);
        return this;
    }

    @Override
    public UpdateFieldPolicyImpl expressions(Expression... expressions) {
        this.expressions = new ArrayList<>(List.of(expressions));
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
    public Expression[] getExpressions() {
        return expressions.toArray(Expression[]::new);
    }

    @Override
    public UpdateFieldPolicyImpl clearIgnoreNulls() {
        ignoreNulls.clear();
        return this;
    }

    @Override
    public UpdateFieldPolicyImpl clearExpressions() {
        expressions.clear();
        return this;
    }

    @Override
    public UpdateFieldPolicyImpl removeIgnoreNull(String fieldName) {
        ignoreNulls.remove(fieldName);
        return this;
    }

    @Override
    public UpdateFieldPolicyImpl expression(String fieldName, String expression) {
        this.expressions.add(new Expression(fieldName, expression));
        return this;
    }

}
