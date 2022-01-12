package cool.scx.util.zip;

import cool.scx.util.tree.ScxTree;

import java.util.List;

/**
 * <p>IVirtualFile interface.</p>
 *
 * @author scx567888
 * @version 1.7.3
 */
public abstract class AbstractVirtualFile implements ScxTree<AbstractVirtualFile> {

    final String name;
    final List<AbstractVirtualFile> children;
    AbstractVirtualFile parent;

    public AbstractVirtualFile(String name, List<AbstractVirtualFile> children) {
        this.name = name;
        this.children = children;
    }

    public final void setParent(AbstractVirtualFile parent) {
        this.parent = parent;
    }

    @Override
    public final AbstractVirtualFile parent() {
        return this.parent;
    }

    @Override
    public final List<AbstractVirtualFile> children() {
        return children;
    }

}
