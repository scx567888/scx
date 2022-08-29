package cool.scx.core;

import cool.scx.core.base.BaseModel;
import cool.scx.core.base.BaseModelService;
import cool.scx.core.base.BaseWebSocketHandler;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static cool.scx.core.ScxHelper.*;
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
    protected final List<Class<?>> allClassList;

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
     * 所有 标识 ScxModel 注解并且 继承自 BaseModel 的 class
     */
    protected final List<Class<? extends BaseModel>> defaultScxBaseModelClassList;

    /**
     * 所有 标识 ScxService 注解并且 继承自 BaseModelService 且 泛型参数不为空 的 class
     */
    protected final List<Class<? extends BaseModelService<?>>> defaultScxBaseModelServiceClassList;

    /**
     * 所有 标识 ScxMapping 注解的 class
     */
    protected final List<Class<?>> defaultScxMappingClassList;

    /**
     * 所有 标识 ScxWebSocketRoute 注解并且 继承自 BaseWebSocketHandler 的 class
     */
    protected final List<Class<? extends BaseWebSocketHandler>> defaultScxWebSocketRouteClassList;

    /**
     * 所有 需要注册到 spring 中的 class
     * 一般就是 scxModelClassList, scxServiceClassList, scxMappingClassList, scxWebSocketRouteClassList 这些类的总和
     */
    protected final List<Class<?>> defaultScxBeanClassList;


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
            this.allClassList = filterByBasePackage(list, basePackage);
            this.rootPath = classSourcePath.getParent();
        } else {
            var list = wrap(() -> getClassListByDir(classSource, getClass().getClassLoader()));
            this.allClassList = filterByBasePackage(list, basePackage);
            this.rootPath = classSourcePath;
        }
        this.defaultScxBaseModelClassList = filterScxBaseModelClassList(this.allClassList);
        this.defaultScxBaseModelServiceClassList = filterScxBaseModelServiceClassList(this.allClassList);
        this.defaultScxMappingClassList = filterScxMappingClassList(this.allClassList);
        this.defaultScxWebSocketRouteClassList = filterScxWebSocketRouteClassList(this.allClassList);
        this.defaultScxBeanClassList = ScxHelper.filterBeanClassList(this.allClassList);
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
     * <p>scxBaseModelClassList.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<Class<? extends BaseModel>> scxBaseModelClassList() {
        return new ArrayList<>(this.defaultScxBaseModelClassList);
    }

    /**
     * <p>scxBaseModelServiceClassList.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<Class<? extends BaseModelService<?>>> scxBaseModelServiceClassList() {
        return new ArrayList<>(this.defaultScxBaseModelServiceClassList);
    }

    /**
     * <p>scxMappingClassList.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<Class<?>> scxMappingClassList() {
        return new ArrayList<>(this.defaultScxMappingClassList);
    }

    /**
     * <p>scxWebSocketRouteClassList.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<Class<? extends BaseWebSocketHandler>> scxWebSocketRouteClassList() {
        return new ArrayList<>(this.defaultScxWebSocketRouteClassList);
    }

    /**
     * <p>scxBeanClassList.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<Class<?>> scxBeanClassList() {
        return new ArrayList<>(this.defaultScxBeanClassList);
    }

    /**
     * <p>allClassList.</p>
     *
     * @return a {@link java.util.List} object
     */
    public final List<Class<?>> allClassList() {
        return new ArrayList<>(this.allClassList);
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
