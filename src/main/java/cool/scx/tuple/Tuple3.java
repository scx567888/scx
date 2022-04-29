package cool.scx.tuple;

public record Tuple3<A, B, C>(A value0, B value1, C value2) implements ScxTuple {

    @Override
    public Object value(int pos) {
        return switch (pos) {
            case 0 -> value0();
            case 1 -> value1();
            case 2 -> value2();
            default -> throw new IllegalArgumentException("索引越界 !!!");
        };
    }

}
