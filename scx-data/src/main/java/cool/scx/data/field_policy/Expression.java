package cool.scx.data.field_policy;

public class Expression {

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

}
