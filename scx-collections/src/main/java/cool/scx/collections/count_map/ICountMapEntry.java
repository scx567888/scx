package cool.scx.collections.count_map;

/// ICountMapEntry
///
/// @param <K> K
/// @author scx567888
/// @version 0.0.1
public interface ICountMapEntry<K> {

    K key();

    long count();

}
