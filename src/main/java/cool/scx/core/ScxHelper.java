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

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;

public final class ScxHelper {

    /**
     * Constant <code>beanFilterAnnotation</code>
     */
    private static final List<Class<? extends Annotation>> beanFilterAnnotation = List.of(
            //scx 注解
            ScxComponent.class, ScxMapping.class, ScxModel.class, ScxService.class, ScxWebSocketMapping.class,
            //兼容 spring 注解
            Component.class, Controller.class, Service.class, Repository.class);

    /**
     * 拥有 scx 注解
     *
     * @param clazz class
     * @return b
     */
    public static boolean isBeanClass(Class<?> clazz) {
        for (var a : beanFilterAnnotation) {
            if (clazz.getAnnotation(a) != null) {
                return true;
            }
        }
        return false;
    }


    /**
     * 初始化需要注入到 spring 中的类
     *
     * @param allClassList a
     * @return a
     */
    public static List<Class<?>> filterBeanClassList(List<Class<?>> allClassList) {
        return allClassList.stream().filter(ScxHelper::isBeanClass).toList(); // 所有标注 Scx 注解的都是需要注入的 BeanClass
    }


    /**
     * 初始化 ScxWebSocketRouteClassList
     *
     * @param allClassList all
     * @return a
     */
    @SuppressWarnings("unchecked")
    static List<Class<? extends BaseWebSocketHandler>> filterScxWebSocketRouteClassList(List<Class<?>> allClassList) {
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
    static List<Class<?>> filterScxMappingClassList(List<Class<?>> allClassList) {
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
    static List<Class<? extends BaseModelService<?>>> filterScxBaseModelServiceClassList(List<Class<?>> allClassList) {
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
    static List<Class<? extends BaseModel>> filterScxBaseModelClassList(List<Class<?>> allClassList) {
        return allClassList.stream().filter(c -> c.isAnnotationPresent(ScxModel.class) // 拥有注解
                        && ScanClassUtils.isInstantiableClass(c) // 是一个可以不需要其他参数直接生成实例化的对象
                        && BaseModel.class.isAssignableFrom(c))// 继承自 BaseModel
                .map(c -> (Class<? extends BaseModel>) c).collect(Collectors.toList());
    }

}
