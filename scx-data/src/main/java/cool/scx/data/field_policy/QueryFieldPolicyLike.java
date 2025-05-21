package cool.scx.data.field_policy;

@SuppressWarnings("unchecked")
public abstract class QueryFieldPolicyLike<QL extends QueryFieldPolicyLike<QL>> implements QueryFieldPolicy {

    private QueryFieldPolicyImpl queryFieldPolicy;

    private QueryFieldPolicyImpl queryFieldPolicy() {
        if (queryFieldPolicy == null) {
            queryFieldPolicy = toQueryFieldPolicy();
        }
        return queryFieldPolicy;
    }

    @Override
    public QL virtualFields(VirtualField... virtualFields) {
        queryFieldPolicy().virtualFields(virtualFields);
        return (QL) this;
    }

    @Override
    public VirtualField[] getVirtualFields() {
        return queryFieldPolicy().getVirtualFields();
    }

    @Override
    public QL clearVirtualFields() {
        queryFieldPolicy().clearVirtualFields();
        return (QL) this;
    }

    @Override
    public QL virtualField(String expression, String virtualFieldName) {
        queryFieldPolicy().virtualField(expression, virtualFieldName);
        return (QL) this;
    }

    @Override
    public QL virtualField(String expression) {
        queryFieldPolicy().virtualField(expression);
        return (QL) this;
    }

    @Override
    public QL include(String... fieldNames) {
        queryFieldPolicy().include(fieldNames);
        return (QL) this;
    }

    @Override
    public QL exclude(String... fieldNames) {
        queryFieldPolicy().exclude(fieldNames);
        return (QL) this;
    }

    @Override
    public FilterMode getFilterMode() {
        return queryFieldPolicy().getFilterMode();
    }

    @Override
    public String[] getFieldNames() {
        return queryFieldPolicy().getFieldNames();
    }

    @Override
    public QL clearFieldNames() {
        queryFieldPolicy().clearFieldNames();
        return (QL) this;
    }

    protected abstract QueryFieldPolicyImpl toQueryFieldPolicy();


}
