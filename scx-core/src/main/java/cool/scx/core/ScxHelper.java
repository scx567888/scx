package cool.scx.core;

import cool.scx.core.annotation.*;
import cool.scx.core.base.BaseModel;
import cool.scx.dao.annotation.ScxModel;
import cool.scx.mvc.annotation.ScxMapping;
import cool.scx.mvc.annotation.ScxWebSocketMapping;
import cool.scx.util.ClassUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * <p>ScxHelper class.</p>
 *
 * @author scx567888
 * @version 1.16.4
 */
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
     * 初始化 ScxModelClassList
     *
     * @param c a
     * @return a
     */
    public static boolean isScxBaseModelClass(Class<?> c) {
        return c.isAnnotationPresent(ScxModel.class) // 拥有注解
                && ClassUtils.isInstantiableClass(c) // 是一个可以不需要其他参数直接生成实例化的对象
                && BaseModel.class.isAssignableFrom(c);
    }

    /**
     * <p>isScxBaseModelServiceClass.</p>
     *
     * @param c a {@link java.lang.Class} object
     * @return a boolean
     */
    public static boolean isScxBaseModelServiceClass(Class<?> c) {
        return c.isAnnotationPresent(ScxService.class) // 拥有注解
                && ClassUtils.isNormalClass(c) // 是一个普通的类 (不是接口, 不是抽象类) ; 此处不要求有必须有无参构造函数 因为此类的创建会由 beanFactory 进行处理
                && c.getGenericSuperclass() instanceof ParameterizedType t //需要有泛型参数
                && t.getActualTypeArguments().length == 1; //并且泛型参数的数量必须是一个
    }


}
