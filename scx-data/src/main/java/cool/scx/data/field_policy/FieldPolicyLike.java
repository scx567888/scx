package cool.scx.data.field_policy;

import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class FieldPolicyLike<FL extends FieldPolicyLike<FL>> implements FieldPolicy {

    private FieldPolicyImpl fieldPolicy;

    private FieldPolicyImpl fieldPolicy() {
        if (fieldPolicy == null) {
            fieldPolicy = toFieldPolicy();
        }
        return fieldPolicy;
    }

    @Override
    public FL virtualFields(VirtualField... virtualFields) {
        fieldPolicy().virtualFields(virtualFields);
        return (FL) this;
    }

    @Override
    public VirtualField[] getVirtualFields() {
        return fieldPolicy().getVirtualFields();
    }

    @Override
    public FL clearVirtualFields() {
        fieldPolicy().clearVirtualFields();
        return (FL) this;
    }

    @Override
    public FL virtualField(String virtualFieldName, String expression) {
        fieldPolicy().virtualField(virtualFieldName, expression);
        return (FL) this;
    }

    @Override
    public FL include(String... fieldNames) {
        fieldPolicy().include(fieldNames);
        return (FL) this;
    }

    @Override
    public FL exclude(String... fieldNames) {
        fieldPolicy().exclude(fieldNames);
        return (FL) this;
    }

    @Override
    public FilterMode getFilterMode() {
        return fieldPolicy().getFilterMode();
    }

    @Override
    public String[] getFieldNames() {
        return fieldPolicy().getFieldNames();
    }

    @Override
    public FL clearFieldNames() {
        fieldPolicy().clearFieldNames();
        return (FL) this;
    }

    @Override
    public FL ignoreNull(boolean ignoreNull) {
        fieldPolicy().ignoreNull(ignoreNull);
        return (FL) this;
    }

    @Override
    public FL ignoreNull(String fieldName, boolean ignoreNull) {
        fieldPolicy().ignoreNull(fieldName, ignoreNull);
        return (FL) this;
    }

    @Override
    public FL expressions(Expression... expressions) {
        fieldPolicy().expressions(expressions);
        return (FL) this;
    }

    @Override
    public boolean getIgnoreNull() {
        return fieldPolicy().getIgnoreNull();
    }

    @Override
    public Map<String, Boolean> getIgnoreNulls() {
        return fieldPolicy().getIgnoreNulls();
    }

    @Override
    public Expression[] getExpressions() {
        return fieldPolicy().getExpressions();
    }

    @Override
    public FL clearIgnoreNulls() {
        fieldPolicy().clearIgnoreNulls();
        return (FL) this;
    }

    @Override
    public FL clearExpressions() {
        fieldPolicy().clearExpressions();
        return (FL) this;
    }

    @Override
    public FL removeIgnoreNull(String fieldName) {
        fieldPolicy().removeIgnoreNull(fieldName);
        return (FL) this;
    }

    @Override
    public FL expression(String fieldName, String expression) {
        fieldPolicy().expression(fieldName, expression);
        return (FL) this;
    }

    protected abstract FieldPolicyImpl toFieldPolicy();

}
