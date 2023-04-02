package cool.scx.orm.query;

public final class AND extends Logic {

    public AND(Object... clauses) {
        super(clauses);
    }

    @Override
    public String keyWord() {
        return "AND";
    }

}