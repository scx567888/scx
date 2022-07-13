package cool.scx.core;

import cool.scx.core.annotation.*;
import cool.scx.core.base.BaseModel;
import cool.scx.core.base.BaseModelService;
import cool.scx.core.base.BaseWebSocketHandler;
import cool.scx.util.ScanClassUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ScxModuleMetadata 用于描述 ScxModule 实例的基本信息
 *
 * @author scx567888
 * @version 1.1.2
 */
public final class ScxModuleMetadata<T extends ScxModule> implements Serializable {

    /**
     * Constant <code>beanFilterAnnotation</code>
     */
    private static final List<Class<? extends Annotation>> beanFilterAnnotation = List.of(
            //scx 注解
            ScxComponent.class, ScxMapping.class, ScxModel.class, ScxService.class, ScxWebSocketMapping.class,
            //兼容 spring 注解
            Component.class, Controller.class, Service.class, Repository.class);

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
    private final List<Class<?>> beanClassList;

    /**
     * 模块根路径
     * 如果模块是 jar 就获取 jar 所在目录
     * 如果 模块不是 jar 就获取 所在 class 的目录
     */
    private final Path moduleRootPath;

    /**
     * 根据 scxModule 实例 创建 ScxModuleMetadata
     *
     * @param scxModuleExample b
     * @throws java.net.URISyntaxException if any.
     * @throws java.io.IOException         if any.
     */
    @SuppressWarnings("unchecked")
    public ScxModuleMetadata(T scxModuleExample) throws URISyntaxException, IOException {
        this.scxModuleClass = (Class<T>) scxModuleExample.getClass();
        this.basePackage = this.scxModuleClass.getPackageName();
        this.scxModuleName = scxModuleExample.name();
        this.scxModuleExample = scxModuleExample;
        var classSource = ScanClassUtils.getClassSource(this.scxModuleClass);
        var classSourcePath = Path.of(classSource);
        //判断当前是否处于 jar 包中
        if (ScanClassUtils.isJar(classSourcePath)) {
            var allClassList = ScanClassUtils.getClassListByJar(classSource);
            this.allClassList = ScanClassUtils.filterByBasePackage(allClassList, this.basePackage);
            this.moduleRootPath = classSourcePath.getParent();
        } else {
            var allClassList = ScanClassUtils.getClassListByDir(classSource, this.scxModuleClass.getClassLoader());
            this.allClassList = ScanClassUtils.filterByBasePackage(allClassList, this.basePackage);
            this.moduleRootPath = classSourcePath;
        }
        this.scxBaseModelClassList = initScxBaseModelClassList(this.allClassList);
        this.scxBaseModelServiceClassList = initScxBaseModelServiceClassList(this.allClassList);
        this.scxMappingClassList = initScxMappingClassList(this.allClassList);
        this.scxWebSocketRouteClassList = initScxWebSocketRouteClassList(this.allClassList);
        this.beanClassList = initBeanClassList(this.allClassList);
    }

    /**
     * 初始化需要注入到 spring 中的类
     *
     * @param allClassList a
     * @return a
     */
    private static List<Class<?>> initBeanClassList(List<Class<?>> allClassList) {
        return allClassList.stream().filter(ScxModuleMetadata::isBeanClass).toList(); // 所有标注 Scx 注解的都是需要注入的 BeanClass
    }

    /**
     * 拥有 scx 注解
     *
     * @param clazz class
     * @return b
     */
    private static boolean isBeanClass(Class<?> clazz) {
        for (var a : beanFilterAnnotation) {
            if (clazz.getAnnotation(a) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 初始化 ScxWebSocketRouteClassList
     *
     * @param allClassList all
     * @return a
     */
    @SuppressWarnings("unchecked")
    private static List<Class<? extends BaseWebSocketHandler>> initScxWebSocketRouteClassList(List<Class<?>> allClassList) {
        return allClassList.stream().filter(c -> c.isAnnotationPresent(ScxWebSocketMapping.class) // 拥有注解
                        && ScanClassUtils.isNormalClass(c) // 是一个普通的类 (不是接口, 不是抽象类) ; 此处不要求有必须有无参构造函数 因为此类的创建会由 beanFactory 进行处理
                        && BaseWebSocketHandler.class.isAssignableFrom(c)) // 继承自 BaseWebSocketHandler
                .map(c -> (Class<? extends BaseWebSocketHandler>) c).collect(Collectors.toList());
    }

    /**
     * 初始化 ScxMappingClassList
     *
     * @param allClassList a
     * @return a
     */
    private static List<Class<?>> initScxMappingClassList(List<Class<?>> allClassList) {
        return allClassList.stream().filter(c -> (c.isAnnotationPresent(ScxMapping.class) || c.isAnnotationPresent(Controller.class)) //拥有注解
                && ScanClassUtils.isNormalClass(c)).toList(); // 是一个普通的类 (不是接口, 不是抽象类) ; 此处不要求有必须有无参构造函数 因为此类的创建会由 beanFactory 进行处理
    }

    /**
     * 初始化 ScxServiceClassList
     *
     * @param allClassList a
     * @return a
     */
    @SuppressWarnings("unchecked")
    private static List<Class<? extends BaseModelService<?>>> initScxBaseModelServiceClassList(List<Class<?>> allClassList) {
        return allClassList.stream().filter(c -> c.isAnnotationPresent(ScxService.class) // 拥有注解
                        && ScanClassUtils.isNormalClass(c) // 是一个普通的类 (不是接口, 不是抽象类) ; 此处不要求有必须有无参构造函数 因为此类的创建会由 beanFactory 进行处理
                        && c.getGenericSuperclass() instanceof ParameterizedType t //需要有泛型参数
                        && t.getActualTypeArguments().length == 1) //并且泛型参数的数量必须是一个
                .map(c -> (Class<? extends BaseModelService<?>>) c).collect(Collectors.toList());
    }

    /**
     * 初始化 ScxModelClassList
     *
     * @param allClassList a
     * @return a
     */
    @SuppressWarnings("unchecked")
    private static List<Class<? extends BaseModel>> initScxBaseModelClassList(List<Class<?>> allClassList) {
        return allClassList.stream().filter(c -> c.isAnnotationPresent(ScxModel.class) // 拥有注解
                        && ScanClassUtils.isInstantiableClass(c) // 是一个可以不需要其他参数直接生成实例化的对象
                        && BaseModel.class.isAssignableFrom(c))// 继承自 BaseModel
                .map(c -> (Class<? extends BaseModel>) c).collect(Collectors.toList());
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
    public Path moduleRootPath() {
        return moduleRootPath;
    }

    /**
     * a
     *
     * @return a
     */
    public List<Class<? extends BaseModel>> scxBaseModelClassList() {
        return new ArrayList<>(scxBaseModelClassList);
    }

    /**
     * a
     *
     * @return a
     */
    public List<Class<? extends BaseModelService<?>>> scxBaseModelServiceClassList() {
        return new ArrayList<>(scxBaseModelServiceClassList);
    }

    /**
     * a
     *
     * @return a
     */
    public List<Class<?>> scxMappingClassList() {
        return new ArrayList<>(scxMappingClassList);
    }

    /**
     * a
     *
     * @return a
     */
    public List<Class<? extends BaseWebSocketHandler>> scxWebSocketRouteClassList() {
        return new ArrayList<>(scxWebSocketRouteClassList);
    }

    /**
     * a
     *
     * @return a
     */
    public List<Class<?>> beanClassList() {
        return new ArrayList<>(beanClassList);
    }

}
