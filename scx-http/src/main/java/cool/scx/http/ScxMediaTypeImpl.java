package cool.scx.http;

public class ScxMediaTypeImpl implements ScxMediaType {

    private final String type;
    private final String subtype;
    private final String value;

    public ScxMediaTypeImpl(String type, String subtype) {
        this.type = type;
        this.subtype = subtype;
        this.value = type + "/" + subtype;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public String subtype() {
        return subtype;
    }

    @Override
    public String value() {
        return value;
    }

}
