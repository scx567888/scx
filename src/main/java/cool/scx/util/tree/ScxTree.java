package cool.scx.util.tree;

import java.util.List;

/**
 * 树接口
 *
 * @param <T> T
 * @author scx567888
 * @version 1.7.3
 */
public interface ScxTree<T extends ScxTree<T>> {

    /**
     * <p>parent.</p>
     *
     * @return a T object
     */
    T parent();

    /**
     * <p>children.</p>
     *
     * @return a {@link java.util.List} object
     */
    List<T> children();

}
