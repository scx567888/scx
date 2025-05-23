package cool.scx.data.field_policy;

import static cool.scx.data.field_policy.FilterMode.EXCLUDED;

public class Expression extends FieldPolicyLike<Expression> {

    private final String fieldName;
    private final String expression;

    public Expression(String fieldName, String expression) {
        if (fieldName == null) {
            throw new NullPointerException("fieldName is null");
        }
        if (expression == null) {
            throw new NullPointerException("expression is null");
        }
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
    protected FieldPolicyImpl toFieldPolicy() {
        //排除 0个 就是包含所有
        return new FieldPolicyImpl(EXCLUDED).expressions(this);
    }

}
