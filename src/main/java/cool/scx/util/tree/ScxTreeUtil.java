package cool.scx.util.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>ScxTreeUtil class.</p>
 *
 * @author scx567888
 * @version 1.7.3
 */
public final class ScxTreeUtil {

    /**
     * <p>walk.</p>
     *
     * @param scxTree a T object
     * @param visitor a {@link cool.scx.util.tree.ScxTreeVisitor} object
     * @param <T>     a T class
     * @throws java.lang.Exception if any.
     */
    public static <T extends ScxTree<T>> void walk(final T scxTree, final ScxTreeVisitor<T> visitor) throws Exception {
        walk0(null, scxTree, visitor);
    }

    /**
     * <p>walk0.</p>
     *
     * @param parents        a {@link java.util.List} object
     * @param currentScxTree a T object
     * @param visitor        a {@link cool.scx.util.tree.ScxTreeVisitor} object
     * @param <T>            a T class
     * @throws java.lang.Exception if any.
     */
    private static <T extends ScxTree<T>> void walk0(final List<T> parents, final T currentScxTree, final ScxTreeVisitor<T> visitor) throws Exception {
        visitor.handle(parents, currentScxTree);
        var newParents = parents == null ? new ArrayList<T>() : new ArrayList<>(parents);
        newParents.add(currentScxTree);
        for (var child : currentScxTree.children()) {
            walk0(newParents, child, visitor);
        }
    }

}
