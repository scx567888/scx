package cool.scx.common.util.tree;

import java.util.List;

/**
 * 树模型接口 注意和 {@link ScxTree} 进行区分 两者用途不同
 *
 * @param <T> T
 * @author scx567888
 * @version 0.0.1
 */
public interface ScxTreeModel<T extends ScxTreeModel<T>> extends ScxTree<T> {

    /**
     * 节点 ID
     *
     * @return a T object
     */
    Object id();

    /**
     * 父节点 ID
     *
     * @return a {@link java.util.List} object
     */
    Object parentID();

    /**
     * 设置子节点
     *
     * @param list a
     */
    void children(List<T> list);

    /**
     * 是否为根节点
     *
     * @return a
     */
    default boolean isRoot() {
        return false;
    }

}
