package cool.scx.tuple;

public record Tuple5<A, B, C, D, E>(A value0, B value1, C value2, D value3, E value4) implements ScxTuple {

    @Override
    public Object value(int pos) {
        return switch (pos) {
            case 0 -> value0();
            case 1 -> value1();
            case 2 -> value2();
            case 3 -> value3();
            case 4 -> value4();
            default -> throw new IllegalArgumentException("索引越界 !!!");
        };
    }

}
