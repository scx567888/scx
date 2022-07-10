package cool.scx.util;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Cache<K, V> extends LinkedHashMap<K, V> {

    private final int maxCount;

    public Cache(int maxCount) {
        this.maxCount = maxCount;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxCount;
    }

}
