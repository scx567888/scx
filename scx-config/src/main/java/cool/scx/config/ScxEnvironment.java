package cool.scx.config;

import cool.scx.common.util.ClassUtils;

import java.nio.file.Path;

/**
 * 项目环境
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ScxEnvironment {

    /**
     * 项目根模块 所在路径
     * 默认取 所有自定义模块的最后一个 所在的文件根目录
     */
    private final Path appRootPath;

    /**
     * 临时目录路径
     */
    private final Path tempPath;

    /**
     * 根据 class 推断 class 根目录
     *
     * @param mainClass class
     */
    public ScxEnvironment(Class<?> mainClass) {
        this.appRootPath = initAppRoot(mainClass);
        this.tempPath = getPathByAppRoot("AppRoot:_temp");
    }

    /**
     * 根据 mainClass 初始化 项目根目录
     *
     * @param mainClass m
     * @return f
     */
    private static Path initAppRoot(Class<?> mainClass) {
        return ClassUtils.getAppRoot(mainClass);
    }

    public Path getPathByAppRoot(String path) {
        if (path.startsWith("AppRoot:")) {
            return Path.of(this.appRootPath.toString(), path.substring("AppRoot:".length()));
        } else {
            return Path.of(path);
        }
    }

    /**
     * 获取临时路径
     *
     * @return a
     */
    public Path getTempPath() {
        return tempPath;
    }

    /**
     * 获取临时路径
     *
     * @param paths a
     * @return a
     */
    public Path getTempPath(String... paths) {
        return Path.of(this.tempPath.toString(), paths);
    }

    public Path appRootPath() {
        return appRootPath;
    }

}
