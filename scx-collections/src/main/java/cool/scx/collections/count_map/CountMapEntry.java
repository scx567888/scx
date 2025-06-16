package cool.scx.collections.count_map;

public record CountMapEntry<K>(K key, long count) implements ICountMapEntry<K> {

}
