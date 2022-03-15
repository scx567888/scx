package cool.scx;

import cool.scx.util.ScanClassUtils;

import java.io.File;

/**
 * 项目环境
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class ScxEnvironment {

    /**
     * 项目根模块 所在路径
     * 默认取 所有自定义模块的最后一个 所在的文件根目录
     */
    private final File appRootFile;

    /**
     * 根据 class 推断 class 根目录
     *
     * @param mainClass class
     */
    public ScxEnvironment(Class<?> mainClass) {
        this.appRootFile = initAppRoot(mainClass);
    }

    /**
     * 根据 mainClass 初始化 项目根目录
     *
     * @param mainClass m
     * @return f
     */
    private static File initAppRoot(Class<?> mainClass) {
        try {
            var classSourceFile = new File(ScanClassUtils.getClassSource(mainClass));
            //判断当前是否处于 jar 包中
            if (ScanClassUtils.isJar(classSourceFile)) {
                return classSourceFile.getParentFile();
            } else {
                return classSourceFile;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p>getFileByAppRoot.</p>
     *
     * @param path a {@link java.lang.String} object.
     * @return a {@link java.io.File} object.
     */
    public File getFileByAppRoot(String path) {
        if (path.startsWith("AppRoot:")) {
            return new File(appRootFile, path.substring("AppRoot:".length()));
        } else {
            return new File(path);
        }
    }

}
