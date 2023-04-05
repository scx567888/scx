package cool.scx.data.query;

public final class OR extends Logic {

    public OR(Object... clauses) {
        super(clauses);
    }

    @Override
    public String keyWord() {
        return "OR";
    }

}