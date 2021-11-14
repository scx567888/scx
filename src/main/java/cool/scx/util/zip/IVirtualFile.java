package cool.scx.util.zip;

/**
 * <p>IVirtualFile interface.</p>
 *
 * @author scx567888
 * @version 1.7.3
 */
public interface IVirtualFile {

    /**
     * 是否是一个目录
     *
     * @return s
     */
    boolean isDirectory();

    /**
     * 路径
     *
     * @return v
     */
    String path();

    /**
     * 寻找子节点 没有返回 null
     *
     * @param name s
     * @return s
     */
    IVirtualFile findChildren(String name);

}
