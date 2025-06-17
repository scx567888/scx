package cool.scx.collections.count_map;

/// CountMapEntry
///
/// @param key   key
/// @param count count
/// @param <K>   K
/// @author scx567888
/// @version 0.0.1
record CountMapEntry<K>(K key, long count) implements ICountMapEntry<K> {

}
