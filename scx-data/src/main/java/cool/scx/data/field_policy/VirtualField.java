package cool.scx.data.field_policy;

public class VirtualField {
    
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
    
}
