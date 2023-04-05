package cool.scx.data.query;

public final class AND extends Logic {

    public AND(Object... clauses) {
        super(clauses);
    }

    @Override
    public String keyWord() {
        return "AND";
    }

}