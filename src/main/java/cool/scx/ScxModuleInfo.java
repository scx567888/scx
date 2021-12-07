package cool.scx;

import cool.scx.annotation.ScxMapping;
import cool.scx.annotation.ScxModel;
import cool.scx.annotation.ScxService;
import cool.scx.annotation.ScxWebSocketMapping;
import cool.scx.base.BaseModel;
import cool.scx.base.BaseModelService;
import cool.scx.base.BaseWebSocketHandler;
import cool.scx.util.ScanClassUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * ScxModuleInfo 用于描述 ScxModule 实例的基本信息
 *
 * @author scx567888
 * @version 1.1.2
 */
public final class ScxModuleInfo<T extends ScxModule> implements Serializable {


    /**
     * 模块名称 用于区分模块 (不允许重复)
     */
    private final String scxModuleName;

    /**
     * 需要扫描类的 basePackage
     */
    private final String basePackage;

    /**
     * ScxModule 实例 用于执行生命周期
     */
    private final T scxModuleExample;

    /**
     * scxModuleClass 用于反向查找
     */
    private final Class<T> scxModuleClass;

    /**
     * 模块中所有的 class
     */
    private final List<Class<?>> allClassList;

    /**
     * 所有 标识 ScxModel 注解并且 继承自 BaseModel 的 class
     */
    private final List<Class<? extends BaseModel>> scxBaseModelClassList;

    /**
     * 所有 标识 ScxService 注解并且 继承自 BaseModelService 且 泛型参数不为空 的 class
     */
    private final List<Class<? extends BaseModelService<?>>> scxBaseModelServiceClassList;

    /**
     * 所有 标识 ScxMapping 注解的 class
     */
    private final List<Class<?>> scxMappingClassList;

    /**
     * 所有 标识 ScxWebSocketRoute 注解并且 继承自 BaseWebSocketHandler 的 class
     */
    private final List<Class<? extends BaseWebSocketHandler>> scxWebSocketRouteClassList;

    /**
     * 所有 需要注册到 spring 中的 class
     * 一般就是 scxModelClassList, scxServiceClassList, scxMappingClassList, scxWebSocketRouteClassList 这些类的总和
     */
    private final List<Class<?>> needRegisterBeanClassList;

    /**
     * 模块根路径
     * 如果模块是 jar 就获取 jar 所在目录
     * 如果 模块不是 jar 就获取 所在 class 的目录
     */
    private final File moduleRootPath;

    /**
     * 根据 scxModule 实例 创建 ScxModuleInfo
     *
     * @param scxModuleExample b
     * @throws java.net.URISyntaxException if any.
     * @throws java.io.IOException         if any.
     */
    @SuppressWarnings("unchecked")
    public ScxModuleInfo(T scxModuleExample) throws URISyntaxException, IOException {
        var moduleClass = (Class<T>) scxModuleExample.getClass();
        this.scxModuleClass = moduleClass;
        this.basePackage = moduleClass.getPackageName();
        this.scxModuleName = scxModuleExample.name();
        this.scxModuleExample = scxModuleExample;
        var classSource = ScanClassUtils.getClassSource(moduleClass);
        var classSourceFile = new File(classSource);
        //判断当前是否处于 jar 包中
        if (ScanClassUtils.isJar(classSourceFile)) {
            var allClassList = ScanClassUtils.getClassListByJar(classSource);
            this.allClassList = ScanClassUtils.filterByBasePackage(allClassList, basePackage);
            this.moduleRootPath = classSourceFile.getParentFile();
        } else {
            var allClassList = ScanClassUtils.getClassListByDir(classSource, moduleClass.getClassLoader());
            this.allClassList = ScanClassUtils.filterByBasePackage(allClassList, basePackage);
            this.moduleRootPath = classSourceFile;
        }
        this.scxBaseModelClassList = initScxBaseModelClassList(this.allClassList);
        this.scxBaseModelServiceClassList = initScxBaseModelServiceClassList(this.allClassList);
        this.scxMappingClassList = initScxMappingClassList(this.allClassList);
        this.scxWebSocketRouteClassList = initScxWebSocketRouteClassList(this.allClassList);
        this.needRegisterBeanClassList = initNeedRegisterBeanClassList(this.allClassList);
    }

    /**
     * 初始化需要注入到 spring 中的类
     *
     * @param allClassList a
     * @return a
     */
    private static List<Class<?>> initNeedRegisterBeanClassList(List<Class<?>> allClassList) {
        var tempList = new ArrayList<Class<?>>();
        for (Class<?> c : allClassList) {
            if (hasScxAnnotation(c)) {
                tempList.add(c);
            }
        }
        return tempList;
    }

    /**
     * 初始化 ScxWebSocketRouteClassList
     *
     * @param allClassList all
     * @return a
     */
    @SuppressWarnings("unchecked")
    private static List<Class<? extends BaseWebSocketHandler>> initScxWebSocketRouteClassList(List<Class<?>> allClassList) {
        var tempList = new ArrayList<Class<? extends BaseWebSocketHandler>>();
        for (Class<?> c : allClassList) {
            if (c.isAnnotationPresent(ScxWebSocketMapping.class) && !c.isInterface() && BaseWebSocketHandler.class.isAssignableFrom(c)) {
                tempList.add((Class<? extends BaseWebSocketHandler>) c);
            }
        }
        return tempList;
    }

    /**
     * 初始化 ScxMappingClassList
     *
     * @param allClassList a
     * @return a
     */
    private static List<Class<?>> initScxMappingClassList(List<Class<?>> allClassList) {
        var tempList = new ArrayList<Class<?>>();
        for (Class<?> c : allClassList) {
            if (c.isAnnotationPresent(ScxMapping.class) && !c.isInterface()) {
                tempList.add(c);
            }
        }
        return tempList;
    }

    /**
     * 初始化 ScxServiceClassList
     *
     * @param allClassList a
     * @return a
     */
    @SuppressWarnings("unchecked")
    private static List<Class<? extends BaseModelService<?>>> initScxBaseModelServiceClassList(List<Class<?>> allClassList) {
        var tempList = new ArrayList<Class<? extends BaseModelService<?>>>();
        for (Class<?> c : allClassList) {
            if (c.isAnnotationPresent(ScxService.class) && !c.isInterface() && BaseModelService.class.isAssignableFrom(c)) {
                //这里获取 泛型类
                var genericSuperclass = c.getGenericSuperclass();
                //只有拥有泛型参数的 并且 参数符合 条件 的才加入到 列表中
                if (genericSuperclass instanceof ParameterizedType) {
                    var typeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
                    if (typeArguments.length == 1) {
                        tempList.add((Class<? extends BaseModelService<?>>) c);
                    }
                }
            }
        }
        return tempList;
    }

    /**
     * 初始化 ScxModelClassList
     *
     * @param allClassList a
     * @return a
     */
    @SuppressWarnings("unchecked")
    private static List<Class<? extends BaseModel>> initScxBaseModelClassList(List<Class<?>> allClassList) {
        var tempList = new ArrayList<Class<? extends BaseModel>>();
        for (Class<?> c : allClassList) {
            if (c.isAnnotationPresent(ScxModel.class) && !c.isInterface() && BaseModel.class.isAssignableFrom(c)) {
                tempList.add((Class<? extends BaseModel>) c);
            }
        }
        return tempList;
    }

    /**
     * 拥有 scx 注解
     *
     * @param clazz class
     * @return b
     */
    private static boolean hasScxAnnotation(Class<?> clazz) {
        return clazz.isAnnotationPresent(ScxService.class)
                || clazz.isAnnotationPresent(ScxMapping.class)
                || clazz.isAnnotationPresent(ScxModel.class)
                || clazz.isAnnotationPresent(ScxWebSocketMapping.class);
    }

    /**
     * <p>scxModuleName.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String scxModuleName() {
        return scxModuleName;
    }

    /**
     * <p>basePackage.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String basePackage() {
        return basePackage;
    }

    /**
     * <p>scxModuleExample 实例.</p>
     *
     * @return a T object
     */
    public T scxModuleExample() {
        return scxModuleExample;
    }

    /**
     * <p>scxModuleClass.</p>
     *
     * @return a {@link java.lang.Class} object
     */
    public Class<T> scxModuleClass() {
        return scxModuleClass;
    }

    /**
     * <p>classList.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<Class<?>> allClassList() {
        return allClassList;
    }

    /**
     * <p>Getter for the field <code>moduleRootPath</code>.</p>
     *
     * @return a {@link java.io.File} object
     */
    public File moduleRootPath() {
        return moduleRootPath;
    }

    /**
     * a
     *
     * @return a
     */
    public List<Class<? extends BaseModel>> scxBaseModelClassList() {
        return scxBaseModelClassList;
    }

    /**
     * a
     *
     * @return a
     */
    public List<Class<? extends BaseModelService<?>>> scxBaseModelServiceClassList() {
        return scxBaseModelServiceClassList;
    }

    /**
     * a
     *
     * @return a
     */
    public List<Class<?>> scxMappingClassList() {
        return scxMappingClassList;
    }

    /**
     * a
     *
     * @return a
     */
    public List<Class<? extends BaseWebSocketHandler>> scxWebSocketRouteClassList() {
        return scxWebSocketRouteClassList;
    }

    /**
     * a
     *
     * @return a
     */
    public List<Class<?>> needRegisterBeanClassList() {
        return needRegisterBeanClassList;
    }

}
