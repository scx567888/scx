package cool.scx.tuple;

/**
 * KeyValue , 和 {@link cool.scx.tuple.Tuple2} 行为完全一致 只作为语义上的区分
 *
 * @param key   key
 * @param value value
 * @param <K>   K
 * @param <V>   V
 * @author scx567888
 * @version 0.0.1
 */
public record KeyValue<K, V>(K key, V value) {

}

