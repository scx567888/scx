package cool.scx.collections.multi_map;

import java.util.List;

/// IMultiMapEntry
///
/// @author scx567888
/// @version 0.0.1
public interface IMultiMapEntry<K, V> {

    K key();

    V value();

    List<V> values();

}
