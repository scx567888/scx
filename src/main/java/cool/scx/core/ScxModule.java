package cool.scx.core;

import cool.scx.core.base.BaseModel;
import cool.scx.core.base.BaseModelService;
import cool.scx.core.base.BaseWebSocketHandler;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static cool.scx.core.ScxHelper.*;
import static cool.scx.util.ScanClassUtils.*;
import static cool.scx.util.exception.ScxExceptionHelper.wrap;

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
     * 所有 标识 ScxModel 注解并且 继承自 BaseModel 的 class
     */
    protected final List<Class<? extends BaseModel>> scxBaseModelClassList;

    /**
     * 所有 标识 ScxService 注解并且 继承自 BaseModelService 且 泛型参数不为空 的 class
     */
    protected final List<Class<? extends BaseModelService<?>>> scxBaseModelServiceClassList;

    /**
     * 所有 标识 ScxMapping 注解的 class
     */
    protected final List<Class<?>> scxMappingClassList;

    /**
     * 所有 标识 ScxWebSocketRoute 注解并且 继承自 BaseWebSocketHandler 的 class
     */
    protected final List<Class<? extends BaseWebSocketHandler>> scxWebSocketRouteClassList;

    /**
     * 所有 需要注册到 spring 中的 class
     * 一般就是 scxModelClassList, scxServiceClassList, scxMappingClassList, scxWebSocketRouteClassList 这些类的总和
     */
    protected final List<Class<?>> beanClassList;

    /**
     * 模块根路径
     * 如果模块是 jar 就获取 jar 所在目录
     * 如果 模块不是 jar 就获取 所在 class 的目录
     */
    private final Path rootPath;

    public ScxModule() {
        var basePackage = getClass().getPackageName();
        var classSource = wrap(() -> getClassSource(getClass()));
        var classSourcePath = Path.of(classSource);
        //判断当前是否处于 jar 包中
        if (isJar(classSourcePath)) {
            var allClassList = wrap(() -> getClassListByJar(classSource));
            this.allClassList = filterByBasePackage(allClassList, basePackage);
            this.rootPath = classSourcePath.getParent();
        } else {
            var allClassList = wrap(() -> getClassListByDir(classSource, getClass().getClassLoader()));
            this.allClassList = filterByBasePackage(allClassList, basePackage);
            this.rootPath = classSourcePath;
        }
        this.scxBaseModelClassList = filterScxBaseModelClassList(this.allClassList);
        this.scxBaseModelServiceClassList = filterScxBaseModelServiceClassList(this.allClassList);
        this.scxMappingClassList = filterScxMappingClassList(this.allClassList);
        this.scxWebSocketRouteClassList = filterScxWebSocketRouteClassList(this.allClassList);
        this.beanClassList = ScxHelper.filterBeanClassList(this.allClassList);
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
        return this.getClass().getSimpleName();
    }

    public final List<Class<?>> allClassList() {
        return allClassList;
    }

    public final Path rootPath() {
        return rootPath;
    }

    public final List<Class<? extends BaseModel>> scxBaseModelClassList() {
        return new ArrayList<>(scxBaseModelClassList);
    }

    public final List<Class<? extends BaseModelService<?>>> scxBaseModelServiceClassList() {
        return new ArrayList<>(scxBaseModelServiceClassList);
    }

    public final List<Class<?>> scxMappingClassList() {
        return new ArrayList<>(scxMappingClassList);
    }

    public final List<Class<? extends BaseWebSocketHandler>> scxWebSocketRouteClassList() {
        return new ArrayList<>(scxWebSocketRouteClassList);
    }

    public final List<Class<?>> beanClassList() {
        return new ArrayList<>(beanClassList);
    }

}
