package cool.scx.tuple;

/**
 * 和 {@link Tuple2} 行为完全一致 只作为语义上的区分
 *
 * @param key   k
 * @param value v
 * @param <A>   a
 * @param <B>   b
 */
public record KeyValue<A, B>(A key, B value) {

}

