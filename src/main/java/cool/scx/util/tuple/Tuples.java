package cool.scx.util.tuple;

/**
 * 一套简单的元组实现
 * 默认提供包含 从 2 个参数到 9 个参数的元组 (因 1 个元素的元组并无实际意义所以并未提供)
 * 注意 !!! 因 java 并没有原生支持元组 所以此处的元组仅作为一种不得已的实现
 * 如果您的数据结构需要明确的有意义的字段命名 建议自行创建一个 record 并使用
 */
public final class Tuples {

    /**
     * @param key   a
     * @param value a
     * @param <A>   a
     * @param <B>   a
     * @return a
     */
    public static <A, B> KeyValue<A, B> keyValue(A key, B value) {
        return new KeyValue<>(key, value);
    }

    /**
     * a
     *
     * @param value0 a
     * @param value1 a
     * @param <A>    a
     * @param <B>    a
     * @return a
     */
    public static <A, B> Tuple2<A, B> of(A value0, B value1) {
        return new Tuple2<>(value0, value1);
    }

    /**
     * a
     *
     * @param value0 a
     * @param value1 a
     * @param value2 a
     * @param <A>    a
     * @param <B>    a
     * @param <C>    a
     * @return a
     */
    public static <A, B, C> Tuple3<A, B, C> of(A value0, B value1, C value2) {
        return new Tuple3<>(value0, value1, value2);
    }

    /**
     * a
     *
     * @param value0 a
     * @param value1 a
     * @param value2 a
     * @param value3 a
     * @param <A>    a
     * @param <B>    a
     * @param <C>    a
     * @param <D>    a
     * @return a
     */
    public static <A, B, C, D> Tuple4<A, B, C, D> of(A value0, B value1, C value2, D value3) {
        return new Tuple4<>(value0, value1, value2, value3);
    }

    /**
     * a
     *
     * @param value0 a
     * @param value1 a
     * @param value2 a
     * @param value3 a
     * @param value4 a
     * @param <A>    a
     * @param <B>    a
     * @param <C>    a
     * @param <D>    a
     * @param <E>    a
     * @return a
     */
    public static <A, B, C, D, E> Tuple5<A, B, C, D, E> of(A value0, B value1, C value2, D value3, E value4) {
        return new Tuple5<>(value0, value1, value2, value3, value4);
    }

    /**
     * a
     *
     * @param value0 a
     * @param value1 a
     * @param value2 a
     * @param value3 a
     * @param value4 a
     * @param value5 a
     * @param <A>    a
     * @param <B>    a
     * @param <C>    a
     * @param <D>    a
     * @param <E>    a
     * @param <F>    a
     * @return a
     */
    public static <A, B, C, D, E, F> Tuple6<A, B, C, D, E, F> of(A value0, B value1, C value2, D value3, E value4, F value5) {
        return new Tuple6<>(value0, value1, value2, value3, value4, value5);
    }

    /**
     * a
     *
     * @param value0 a
     * @param value1 a
     * @param value2 a
     * @param value3 a
     * @param value4 a
     * @param value5 a
     * @param value6 a
     * @param <A>    a
     * @param <B>    a
     * @param <C>    a
     * @param <D>    a
     * @param <E>    a
     * @param <F>    a
     * @param <G>    a
     * @return a
     */
    public static <A, B, C, D, E, F, G> Tuple7<A, B, C, D, E, F, G> of(A value0, B value1, C value2, D value3, E value4, F value5, G value6) {
        return new Tuple7<>(value0, value1, value2, value3, value4, value5, value6);
    }

    /**
     * a
     *
     * @param value0 a
     * @param value1 a
     * @param value2 a
     * @param value3 a
     * @param value4 a
     * @param value5 a
     * @param value6 a
     * @param value7 a
     * @param <A>    a
     * @param <B>    a
     * @param <C>    a
     * @param <D>    a
     * @param <E>a
     * @param <F>    a
     * @param <G>    a
     * @param <H>    a
     * @return a
     */
    public static <A, B, C, D, E, F, G, H> Tuple8<A, B, C, D, E, F, G, H> of(A value0, B value1, C value2, D value3, E value4, F value5, G value6, H value7) {
        return new Tuple8<>(value0, value1, value2, value3, value4, value5, value6, value7);
    }

    /**
     * a
     *
     * @param value0 a
     * @param value1 a
     * @param value2 a
     * @param value3 a
     * @param value4 a
     * @param value5 a
     * @param value6 a
     * @param value7 a
     * @param value8 a
     * @param <A>    a
     * @param <B>    a
     * @param <C>    a
     * @param <D>    a
     * @param <E>    a
     * @param <F>    a
     * @param <G>    a
     * @param <H>    a
     * @param <I>    a
     * @return a
     */
    public static <A, B, C, D, E, F, G, H, I> Tuple9<A, B, C, D, E, F, G, H, I> of(A value0, B value1, C value2, D value3, E value4, F value5, G value6, H value7, I value8) {
        return new Tuple9<>(value0, value1, value2, value3, value4, value5, value6, value7, value8);
    }

}