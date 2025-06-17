package cool.scx.http.parameters;

/// ParametersWritable
///
/// @author scx567888
/// @version 0.0.1
@SuppressWarnings("unchecked")
public interface ParametersWritable<K, V> extends Parameters<K, V> {

    ParametersWritable<K, V> set(K name, V... value);

    ParametersWritable<K, V> add(K name, V... value);

    ParametersWritable<K, V> remove(K name);

    ParametersWritable<K, V> clear();

}
