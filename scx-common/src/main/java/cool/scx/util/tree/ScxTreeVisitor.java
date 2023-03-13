package cool.scx.util.tree;

import java.util.List;

/**
 * 树 访问器
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface ScxTreeVisitor<T extends ScxTree<T>> {

    /**
     * <p>handle.</p>
     *
     * @param parents        a {@link java.util.List} object
     * @param currentScxTree a T object
     */
    void handle(List<T> parents, T currentScxTree);

}
