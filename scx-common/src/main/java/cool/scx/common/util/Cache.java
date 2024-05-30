package cool.scx.common.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 固定大小的简单缓存 线程不安全 !!! (永久储存, 达到最大容量后移除之前的元素)
 *
 * @param <K> a
 * @param <V> a
 * @author scx567888
 * @version 0.0.1
 */
public final class Cache<K, V> extends LinkedHashMap<K, V> {

    /**
     * 最大容量
     */
    private final int maxSize;

    public Cache(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }

}
