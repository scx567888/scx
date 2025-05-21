package cool.scx.data.field_policy;

import static cool.scx.data.field_policy.FilterMode.EXCLUDED;

public class Expression extends UpdateFieldPolicyLike<Expression> {

    private final String fieldName;
    private final String expression;

    public Expression(String fieldName, String expression) {
        this.fieldName = fieldName;
        this.expression = expression;
    }

    public String fieldName() {
        return fieldName;
    }

    public String expression() {
        return expression;
    }

    @Override
    protected UpdateFieldPolicyImpl toUpdateFieldPolicy() {
        //排除 0个 就是包含所有
        return new UpdateFieldPolicyImpl(EXCLUDED).expressions(this);
    }

}
