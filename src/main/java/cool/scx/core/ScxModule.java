package cool.scx.core;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static cool.scx.util.ScanClassUtils.*;
import static cool.scx.util.ScxExceptionHelper.wrap;

/**
 * Scx 模块接口 , 自定义模块必须实现此接口
 * <p>
 * 当自定义的模块实现此接口之后 , 会根据 自定义模块的 根 package 进行扫描 , 所以功能代码请放在自定义模块的包或子包下
 * <p>
 * 生命周期请参阅方法说明
 *
 * @author scx567888
 * @version 1.1.2
 */
public abstract class ScxModule {

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
     * <p>Constructor for ScxModule.</p>
     */
    public ScxModule() {
        var basePackage = getClass().getPackageName();
        var classSource = wrap(() -> getClassSource(getClass()));
        var classSourcePath = Path.of(classSource);
        //判断当前是否处于 jar 包中
        if (isJar(classSourcePath)) {
            var list = wrap(() -> getClassListByJar(classSource));
            this.classList = filterByBasePackage(list, basePackage);
            this.rootPath = classSourcePath.getParent();
        } else {
            var list = wrap(() -> getClassListByDir(classSource, getClass().getClassLoader()));
            this.classList = filterByBasePackage(list, basePackage);
            this.rootPath = classSourcePath;
        }
    }

    /**
     * 核心模块初始化完成调用
     * 注意请不要阻塞此方法
     */
    public void start() {

    }

    /**
     * 项目停止或结束时调用
     * 注意请不要阻塞此方法
     */
    public void stop() {

    }

    /**
     * 模块名称
     *
     * @return name
     */
    public String name() {
        return this.defaultName;
    }

    /**
     * <p>allClassList.</p>
     *
     * @return a {@link java.util.List} object
     */
    public final List<Class<?>> classList() {
        return new ArrayList<>(this.classList);
    }

    /**
     * <p>rootPath.</p>
     *
     * @return a {@link java.nio.file.Path} object
     */
    public final Path rootPath() {
        return this.rootPath;
    }

}
