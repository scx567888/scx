package cool.scx.data.field_policy;

import java.util.LinkedHashMap;
import java.util.Map;

public class QueryFieldPolicyImpl extends FieldPolicyImpl<QueryFieldPolicy> implements QueryFieldPolicy {

    private final Map<String, String> virtualFields;

    public QueryFieldPolicyImpl(FilterMode filterMode) {
        super(filterMode);
        this.virtualFields = new LinkedHashMap<>();
    }

    @Override
    public QueryFieldPolicy virtualField(String expression, String virtualFieldName) {
        this.virtualFields.put(expression, virtualFieldName);
        return this;
    }

    @Override
    public QueryFieldPolicy virtualField(String expression) {
        this.virtualFields.put(expression, null);
        return this;
    }

    @Override
    public Map<String, String> getVirtualFields() {
        return virtualFields;
    }

    @Override
    public QueryFieldPolicy clearVirtualFields() {
        this.virtualFields.clear();
        return this;
    }

}
