package cool.scx.tuple;

/**
 * 一套简单的元组实现
 * 默认提供包含 从 2 个参数到 9 个参数的元组 (因 1 个元素的元组并无实际意义所以并未提供)
 * 注意 !!! 因 java 并没有原生支持元组 所以此处的元组仅作为一种不得已的实现
 * 如果您的数据结构需要明确的有意义的字段命名 建议自行创建一个 record 并使用
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class Tuples {

    /**
     * KeyValue
     *
     * @param key   key
     * @param value value
     * @param <K>   K
     * @param <V>   V
     * @return KeyValue
     */
    public static <K, V> KeyValue<K, V> keyValue(K key, V value) {
        return new KeyValue<>(key, value);
    }

    /**
     * Tuple2
     *
     * @param value0 value0
     * @param value1 value1
     * @param <A>    A
     * @param <B>    B
     * @return Tuple2
     */
    public static <A, B> Tuple2<A, B> of(A value0, B value1) {
        return new Tuple2<>(value0, value1);
    }

    /**
     * Tuple3
     *
     * @param value0 value0
     * @param value1 value1
     * @param value2 value2
     * @param <A>    A
     * @param <B>    B
     * @param <C>    C
     * @return Tuple3
     */
    public static <A, B, C> Tuple3<A, B, C> of(A value0, B value1, C value2) {
        return new Tuple3<>(value0, value1, value2);
    }

    /**
     * Tuple4
     *
     * @param value0 value0
     * @param value1 value1
     * @param value2 value2
     * @param value3 value3
     * @param <A>    A
     * @param <B>    B
     * @param <C>    C
     * @param <D>    D
     * @return Tuple4
     */
    public static <A, B, C, D> Tuple4<A, B, C, D> of(A value0, B value1, C value2, D value3) {
        return new Tuple4<>(value0, value1, value2, value3);
    }

    /**
     * Tuple5
     *
     * @param value0 value0
     * @param value1 value1
     * @param value2 value2
     * @param value3 value3
     * @param value4 value4
     * @param <A>    A
     * @param <B>    B
     * @param <C>    C
     * @param <D>    D
     * @param <E>    E
     * @return Tuple5
     */
    public static <A, B, C, D, E> Tuple5<A, B, C, D, E> of(A value0, B value1, C value2, D value3, E value4) {
        return new Tuple5<>(value0, value1, value2, value3, value4);
    }

    /**
     * Tuple6
     *
     * @param value0 value0
     * @param value1 value1
     * @param value2 value2
     * @param value3 value3
     * @param value4 value4
     * @param value5 value5
     * @param <A>    A
     * @param <B>    B
     * @param <C>    C
     * @param <D>    D
     * @param <E>    E
     * @param <F>    F
     * @return Tuple6
     */
    public static <A, B, C, D, E, F> Tuple6<A, B, C, D, E, F> of(A value0, B value1, C value2, D value3, E value4, F value5) {
        return new Tuple6<>(value0, value1, value2, value3, value4, value5);
    }

    /**
     * Tuple7
     *
     * @param value0 value0
     * @param value1 value1
     * @param value2 value2
     * @param value3 value3
     * @param value4 value4
     * @param value5 value5
     * @param value6 value6
     * @param <A>    A
     * @param <B>    B
     * @param <C>    C
     * @param <D>    D
     * @param <E>    E
     * @param <F>    F
     * @param <G>    G
     * @return Tuple7
     */
    public static <A, B, C, D, E, F, G> Tuple7<A, B, C, D, E, F, G> of(A value0, B value1, C value2, D value3, E value4, F value5, G value6) {
        return new Tuple7<>(value0, value1, value2, value3, value4, value5, value6);
    }

    /**
     * Tuple8
     *
     * @param value0 value0
     * @param value1 value1
     * @param value2 value2
     * @param value3 value3
     * @param value4 value4
     * @param value5 value5
     * @param value6 value6
     * @param value7 value7
     * @param <A>    A
     * @param <B>    B
     * @param <C>    C
     * @param <D>    D
     * @param <E>    E
     * @param <F>    F
     * @param <G>    G
     * @param <H>    H
     * @return Tuple8
     */
    public static <A, B, C, D, E, F, G, H> Tuple8<A, B, C, D, E, F, G, H> of(A value0, B value1, C value2, D value3, E value4, F value5, G value6, H value7) {
        return new Tuple8<>(value0, value1, value2, value3, value4, value5, value6, value7);
    }

    /**
     * Tuple9
     *
     * @param value0 value0
     * @param value1 value1
     * @param value2 value2
     * @param value3 value3
     * @param value4 value4
     * @param value5 value5
     * @param value6 value6
     * @param value7 value7
     * @param value8 value8
     * @param <A>    A
     * @param <B>    B
     * @param <C>    C
     * @param <D>    D
     * @param <E>    E
     * @param <F>    F
     * @param <G>    G
     * @param <H>    H
     * @param <I>    I
     * @return Tuple9
     */
    public static <A, B, C, D, E, F, G, H, I> Tuple9<A, B, C, D, E, F, G, H, I> of(A value0, B value1, C value2, D value3, E value4, F value5, G value6, H value7, I value8) {
        return new Tuple9<>(value0, value1, value2, value3, value4, value5, value6, value7, value8);
    }

}
