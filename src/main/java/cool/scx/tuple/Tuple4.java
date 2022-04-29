package cool.scx.tuple;

public record Tuple4<A, B, C, D>(A value0, B value1, C value2, D value3) implements ScxTuple {

    @Override
    public Object value(int pos) {
        return switch (pos) {
            case 0 -> value0();
            case 1 -> value1();
            case 2 -> value2();
            case 3 -> value3();
            default -> throw new IllegalArgumentException("索引越界 !!!");
        };
    }

}
