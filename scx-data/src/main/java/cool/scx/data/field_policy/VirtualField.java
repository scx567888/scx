package cool.scx.data.field_policy;

import static cool.scx.data.field_policy.FilterMode.EXCLUDED;

public class VirtualField extends FieldPolicyLike<VirtualField> {

    private final String virtualFieldName;
    private final String expression;

    public VirtualField(String virtualFieldName, String expression) {
        if (virtualFieldName == null){
            throw new NullPointerException("virtualFieldName is null");
        }
        if (expression == null){
            throw new NullPointerException("expression is null");
        }
        this.virtualFieldName = virtualFieldName;
        this.expression = expression;
    }

    public String virtualFieldName() {
        return virtualFieldName;
    }

    public String expression() {
        return expression;
    }

    @Override
    protected FieldPolicyImpl toFieldPolicy() {
        //排除 0个 就是包含所有
        return new FieldPolicyImpl(EXCLUDED).virtualFields(this);
    }

}
