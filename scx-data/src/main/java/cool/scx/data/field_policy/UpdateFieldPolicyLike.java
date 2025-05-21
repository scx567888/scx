package cool.scx.data.field_policy;

import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class UpdateFieldPolicyLike<UL extends UpdateFieldPolicyLike<UL>> implements UpdateFieldPolicy {

    private UpdateFieldPolicyImpl updateFieldPolicy;

    private UpdateFieldPolicyImpl updateFieldPolicy() {
        if (updateFieldPolicy == null) {
            updateFieldPolicy = toUpdateFieldPolicy();
        }
        return updateFieldPolicy;
    }

    @Override
    public UL ignoreNull(boolean ignoreNull) {
        updateFieldPolicy().ignoreNull(ignoreNull);
        return (UL) this;
    }

    @Override
    public UL ignoreNull(String fieldName, boolean ignoreNull) {
        updateFieldPolicy().ignoreNull(fieldName, ignoreNull);
        return (UL) this;
    }

    @Override
    public UL expressions(Expression... expressions) {
        updateFieldPolicy().expressions(expressions);
        return (UL) this;
    }

    @Override
    public boolean getIgnoreNull() {
        return updateFieldPolicy().getIgnoreNull();
    }

    @Override
    public Map<String, Boolean> getIgnoreNulls() {
        return updateFieldPolicy().getIgnoreNulls();
    }

    @Override
    public Expression[] getExpressions() {
        return updateFieldPolicy().getExpressions();
    }

    @Override
    public UL clearIgnoreNulls() {
        updateFieldPolicy().clearIgnoreNulls();
        return (UL) this;
    }

    @Override
    public UL clearExpressions() {
        updateFieldPolicy().clearExpressions();
        return (UL) this;
    }

    @Override
    public UL removeIgnoreNull(String fieldName) {
        updateFieldPolicy().removeIgnoreNull(fieldName);
        return (UL) this;
    }

    @Override
    public UL expression(String fieldName, String expression) {
        updateFieldPolicy().expression(fieldName, expression);
        return (UL) this;
    }

    @Override
    public UL include(String... fieldNames) {
        updateFieldPolicy().include(fieldNames);
        return (UL) this;
    }

    @Override
    public UL exclude(String... fieldNames) {
        updateFieldPolicy().exclude(fieldNames);
        return (UL) this;
    }

    @Override
    public FilterMode getFilterMode() {
        return updateFieldPolicy().getFilterMode();
    }

    @Override
    public String[] getFieldNames() {
        return updateFieldPolicy().getFieldNames();
    }

    @Override
    public UL clearFieldNames() {
        updateFieldPolicy().clearFieldNames();
        return (UL) this;
    }

    protected abstract UpdateFieldPolicyImpl toUpdateFieldPolicy();

}
