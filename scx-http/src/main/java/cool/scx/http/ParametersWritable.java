package cool.scx.http;

/**
 * ParametersWritable
 */
public interface ParametersWritable<K, V> extends Parameters<K, V> {

    @SuppressWarnings("unchecked")
    ParametersWritable<K, V> set(K name, V... value);

    @SuppressWarnings("unchecked")
    ParametersWritable<K, V> add(K name, V... value);

    ParametersWritable<K, V> remove(K name);

    ParametersWritable<K, V> clear();

}
