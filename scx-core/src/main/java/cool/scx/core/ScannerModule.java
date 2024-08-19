package cool.scx.core;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static cool.scx.core.ScxHelper.findClassListByScxModule;
import static cool.scx.core.ScxHelper.findRootPathByScxModule;

/**
 * <p>
 * 根据 自定义模块的 根 package 进行扫描 , 所以功能代码请放在自定义模块的包或子包下
 * <p>
 *
 * @author scx567888
 * @version 1.1.2
 */
public abstract class ScannerModule implements ScxModule {

    /**
     * 模块中所有的 class
     */
    protected final List<Class<?>> classList;

    /**
     * 模块根路径
     * 如果模块是 jar 就获取 jar 所在目录
     * 如果 模块不是 jar 就获取 所在 class 的目录
     */
    protected final Path rootPath;

    /**
     * 默认名称
     */
    protected final String defaultName = this.getClass().getSimpleName();

    /**
     * 在模块创建时 加载所有包含的 class
     */
    public ScannerModule() {
        try {
            //这里使用 ArrayList 重新包装一下 以便后续可以修改
            this.classList = new ArrayList<>(findClassListByScxModule(this.getClass()));
            this.rootPath = findRootPathByScxModule(this.getClass());
        } catch (IOException e) {
            throw new RuntimeException("ScannerModule 加载失败 !!!", e);
        }
    }

    /**
     * 模块名称
     *
     * @return name
     */
    @Override
    public String name() {
        return this.defaultName;
    }

    /**
     * 每次都返回一个 新的 list 防止外部修改
     *
     * @return r
     */
    @Override
    public List<Class<?>> classList() {
        return new ArrayList<>(this.classList);
    }

    /**
     * 所处路径
     *
     * @return a
     */
    @Override
    public Path rootPath() {
        return this.rootPath;
    }

}
