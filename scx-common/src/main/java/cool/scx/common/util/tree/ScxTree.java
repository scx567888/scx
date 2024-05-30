package cool.scx.common.util.tree;

import java.util.List;

/**
 * 树接口 注意和 {@link ScxTreeModel} 进行区分 两者用途不同
 *
 * @param <T> T
 * @author scx567888
 * @version 0.0.1
 */
public interface ScxTree<T extends ScxTree<T>> {

    /**
     * 获取 父节点
     *
     * @return 父节点 (默认为空)
     */
    default T parent() {
        return null;
    }

    /**
     * 获取子节点
     *
     * @return 子节点集合
     */
    List<T> children();

}
