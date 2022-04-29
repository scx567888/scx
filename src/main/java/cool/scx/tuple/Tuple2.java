package cool.scx.tuple;

public record Tuple2<A, B>(A value0, B value1) implements ScxTuple {

    @Override
    public Object value(int pos) {
        return switch (pos) {
            case 0 -> value0();
            case 1 -> value1();
            default -> throw new IllegalArgumentException("索引越界 !!!");
        };
    }

}
