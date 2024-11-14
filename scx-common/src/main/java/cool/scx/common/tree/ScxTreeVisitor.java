package cool.scx.common.tree;

import java.util.List;

/**
 * 树 访问器
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface ScxTreeVisitor<T extends ScxTree<T>> {

    /**
     * 访问器
     *
     * @param parents        所有父节点
     * @param currentScxTree 当前节点
     */
    void handle(List<T> parents, T currentScxTree);

}
