package cool.scx.common.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static cool.scx.common.util.ScxExceptionHelper.noException;
import static cool.scx.common.util.StringUtils.endsWithIgnoreCase;

/**
 * 类工具类
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ClassUtils {

    /**
     * 默认 classLoader
     */
    private static final ClassLoader DEFAULT_CLASS_LOADER = ClassUtils.class.getClassLoader();

    /**
     * 判断是否为 Enum , {@link Class#isEnum()} 无法处理内部类的情况
     *
     * @param c c
     * @return isEnum
     */
    public static boolean isEnum(Class<?> c) {
        return Enum.class.isAssignableFrom(c);
    }

    /**
     * 获取 Enum 类的真实类 包括内部类的情况
     *
     * @param c c
     * @return c
     */
    public static Class<?> getEnumClass(Class<?> c) {
        if (isEnum(c)) {
            return c.isAnonymousClass() ? c.getSuperclass() : c;
        } else {
            throw new IllegalArgumentException(c.getName() + ": Not an enum !!!");
        }
    }


    /**
     * 是否是可以实例化的类
     * 如果类的构造函数是私有的 我们便假设此类不想让我们进行实例化
     *
     * @param c c
     * @return c
     */
    public static boolean isInstantiableClass(Class<?> c) {
        //既不是 接口也不是 抽象类
        return isNormalClass(c) && noException(() -> c.getConstructor().newInstance());
    }

    /**
     * 是一个普通类 既不是 接口也不是 抽象类
     *
     * @param c a
     * @return a
     */
    public static boolean isNormalClass(Class<?> c) {
        return !c.isInterface() && !Modifier.isAbstract(c.getModifiers());
    }

    /**
     * 根据 class 获取源地址
     *
     * @param source a {@link java.lang.Class} object.
     * @return 可能是 目录 也可能是 jar 文件
     */
    public static URI getCodeSource(Class<?> source) {
        return URI.create(source.getProtectionDomain().getCodeSource().getLocation().toString());
    }


    /**
     * 根据 codeSource 获取 app 根路径(文件夹)
     *
     * @param codeSource {@link ClassUtils#getCodeSource(Class)}
     * @return app 根路径(文件夹)
     */
    public static Path getAppRoot(URI codeSource) {
        var path = Path.of(codeSource);
        return Files.isDirectory(path) ? path : path.getParent();
    }

    /**
     * 根据 class 获取 app 根路径(文件夹)
     *
     * @param source {@link ClassUtils#getCodeSource(Class)}
     * @return app 根路径(文件夹)
     */
    public static Path getAppRoot(Class<?> source) {
        return getAppRoot(getCodeSource(source));
    }

    /**
     * 判断路径是否是一个 jar 文件 (这里只是简单的使用 文件后缀判断,并不准确)
     *
     * @param path a
     * @return a
     */
    public static boolean isJar(Path path) {
        return Files.isRegularFile(path) && endsWithIgnoreCase(path.toString(), ".jar");
    }

    /**
     * 根据 basePackage 对 class 进行过滤
     *
     * @param classList       a
     * @param basePackageName a
     * @return a
     */
    public static Class<?>[] filterByBasePackage(Class<?>[] classList, String basePackageName) {
        var p = basePackageName + ".";
        return Arrays.stream(classList).filter(c -> c.getPackageName().equals(basePackageName) || c.getPackageName().startsWith(p)).toArray(Class[]::new);
    }

    /**
     * 从 JarEntry 加载 class
     *
     * @param jarEntry       a
     * @param jarClassLoader a
     * @return a
     */
    private static Class<?> loadClassFromJar(JarEntry jarEntry, ClassLoader jarClassLoader) {
        var suffixLength = ".class".length();
        //这里是可以保证 path 最后一定是 .class 所以在此处可以放心移除
        var className = jarEntry.getName().substring(0, jarEntry.getName().length() - suffixLength).replace('/', '.');
        return loadClass0(className, jarClassLoader);
    }

    /**
     * 从 Path 加载 class
     *
     * @param classRealPath a
     * @param classLoader   a
     * @return a
     */
    private static Class<?> loadClassFromPath(Path classRealPath, ClassLoader classLoader) {
        var suffixLength = ".class.".length();
        var str = new StringBuilder();
        for (var path : classRealPath) {
            str.append(path.toString()).append(".");
        }
        //这里会在最后添加一个多余的 . 在这里移除
        var className = str.substring(0, str.length() - suffixLength);
        return loadClass0(className, classLoader);
    }

    /**
     * 加载 class 先使用 默认的 classloader 失败后使用 备选的 classloader
     *
     * @param className         className
     * @param secondClassLoader 备选 classloader
     * @return c
     */
    private static Class<?> loadClass0(String className, ClassLoader secondClassLoader) {
        try {
            return DEFAULT_CLASS_LOADER.loadClass(className);
        } catch (ClassNotFoundException t1) {
            try {
                return secondClassLoader.loadClass(className);
            } catch (ClassNotFoundException t2) {
                return null;
            }
        }
    }

    /**
     * 读取 jar 包中的所有 class
     *
     * @param jarFileURI jar
     * @return r
     * @throws java.io.IOException r
     */
    public static Class<?>[] findClassListFromJar(URI jarFileURI) throws IOException {
        try (var jarFile = new JarFile(new File(jarFileURI)); var jarClassLoader = new URLClassLoader(new URL[]{jarFileURI.toURL()}); var jarEntryStream = jarFile.stream()) {
            return jarEntryStream.filter(jarEntry -> !jarEntry.isDirectory() && jarEntry.getName().endsWith(".class")).map(jarEntry -> loadClassFromJar(jarEntry, jarClassLoader)).toArray(Class[]::new);
        }
    }

    /**
     * 根据文件获取 class 列表
     *
     * @param classRootPath a
     * @param classLoader   a
     * @return a
     * @throws java.io.IOException if any.
     */
    public static Class<?>[] findClassListFromPath(Path classRootPath, ClassLoader classLoader) throws IOException {
        try (var pathStream = Files.walk(classRootPath)) {
            return pathStream.filter(path -> Files.isRegularFile(path) && path.toString().endsWith(".class")).map(path -> loadClassFromPath(classRootPath.relativize(path), classLoader)).toArray(Class[]::new);
        }
    }

}
