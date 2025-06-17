package cool.scx.collections.multi_map;

import java.util.List;

public interface IMultiMapEntry<K, V> {
    
    K key();
    
    V first();
    
    List<V> all();
    
}
