package cool.scx.bean;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.ResolvableType;

public interface BeanFactory {

    Object getBean(String name);

    <T> T getBean(String name, Class<T> requiredType);

    Object getBean(String name, Object... args);

    <T> T getBean(Class<T> requiredType);

    <T> T getBean(Class<T> requiredType, Object... args);

    <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType);

    <T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType);

    boolean containsBean(String name);

    boolean isSingleton(String name);

    boolean isPrototype(String name);

    boolean isTypeMatch(String name, ResolvableType typeToMatch);

    boolean isTypeMatch(String name, Class<?> typeToMatch);

    Class<?> getType(String name);

    Class<?> getType(String name, boolean allowFactoryBeanInit);

    String[] getAliases(String name);

}
