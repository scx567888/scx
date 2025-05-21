package cool.scx.data.field_policy;

import java.util.ArrayList;
import java.util.List;

public class QueryFieldPolicyImpl extends FieldPolicyImpl<QueryFieldPolicy> implements QueryFieldPolicy {

    private List<VirtualField> virtualFields;

    public QueryFieldPolicyImpl(FilterMode filterMode) {
        super(filterMode);
        this.virtualFields = new ArrayList<>();
    }

    @Override
    public QueryFieldPolicy virtualFields(VirtualField... virtualFields) {
        this.virtualFields = new ArrayList<>(List.of(virtualFields));
        return this;
    }

    @Override
    public VirtualField[] getVirtualFields() {
        return virtualFields.toArray(VirtualField[]::new);
    }

    @Override
    public QueryFieldPolicy clearVirtualFields() {
        this.virtualFields.clear();
        return this;
    }

    @Override
    public QueryFieldPolicy virtualField(String expression, String virtualFieldName) {
        this.virtualFields.add(new VirtualField(expression, virtualFieldName));
        return this;
    }

    @Override
    public QueryFieldPolicy virtualField(String expression) {
        this.virtualFields.add(new VirtualField(expression, null));
        return this;
    }

}
