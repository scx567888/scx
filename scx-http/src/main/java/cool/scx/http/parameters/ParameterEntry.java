package cool.scx.http.parameters;

import java.util.List;

public interface ParameterEntry<K, V> {

    K name();
    
    V value();
    
    List<V> values();
    
}
