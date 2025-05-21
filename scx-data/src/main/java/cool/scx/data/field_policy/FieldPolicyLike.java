package cool.scx.data.field_policy;

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
    public FL virtualField(String expression, String virtualFieldName) {
        fieldPolicy().virtualField(expression, virtualFieldName);
        return (FL) this;
    }

    @Override
    public FL virtualField(String expression) {
        fieldPolicy().virtualField(expression);
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

    protected abstract FieldPolicyImpl toFieldPolicy();


}
