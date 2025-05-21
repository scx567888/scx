package cool.scx.data.field_policy;

import static cool.scx.data.field_policy.FilterMode.EXCLUDED;

public class VirtualField extends QueryFieldPolicyLike<VirtualField> {

    private final String expression;
    private final String virtualFieldName;

    public VirtualField(String expression, String virtualFieldName) {
        this.expression = expression;
        this.virtualFieldName = virtualFieldName;
    }

    public String expression() {
        return expression;
    }

    public String virtualFieldName() {
        return virtualFieldName;
    }

    @Override
    protected QueryFieldPolicyImpl toQueryFieldPolicy() {
        //排除 0个 就是包含所有
        return new QueryFieldPolicyImpl(EXCLUDED).virtualFields(this);
    }

}
