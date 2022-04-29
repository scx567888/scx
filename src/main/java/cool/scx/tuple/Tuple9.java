package cool.scx.tuple;

public record Tuple9<A, B, C, D, E, F, G, H, I>(A value0, B value1, C value2, D value3, E value4, F value5, G value6,
                                                H value7, I value8) implements ScxTuple {

    @Override
    public Object value(int pos) {
        return switch (pos) {
            case 0 -> value0();
            case 1 -> value1();
            case 2 -> value2();
            case 3 -> value3();
            case 4 -> value4();
            case 5 -> value5();
            case 6 -> value6();
            case 7 -> value7();
            case 8 -> value8();
            default -> throw new IllegalArgumentException("索引越界 !!!");
        };
    }

}
