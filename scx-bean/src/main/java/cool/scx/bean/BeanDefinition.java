package cool.scx.bean;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.core.ResolvableType;

import java.util.function.Function;

public interface BeanDefinition {

    void setAttribute(String name, Object value);

    Object getAttribute(String name);

    Object removeAttribute(String name);

    boolean hasAttribute(String name);

    String[] attributeNames();

    String getParentName();

    void setParentName(String parentName);

    String getBeanClassName();

    void setBeanClassName(String beanClassName);

    String getScope();

    void setScope(String scope);

    boolean isLazyInit();

    void setLazyInit(boolean lazyInit);

    String[] getDependsOn();

    void setDependsOn(String[] dependsOn);

    boolean isAutowireCandidate();

    void setAutowireCandidate(boolean autowireCandidate);

    boolean isPrimary();

    void setPrimary(boolean primary);

    boolean isFallback();

    void setFallback(boolean fallback);

    String getFactoryBeanName();

    void setFactoryBeanName(String factoryBeanName);

    String getFactoryMethodName();

    void setFactoryMethodName(String factoryMethodName);

    ConstructorArgumentValues getConstructorArgumentValues();

    MutablePropertyValues getPropertyValues();

    String getInitMethodName();

    void setInitMethodName(String initMethodName);

    String getDestroyMethodName();

    void setDestroyMethodName(String destroyMethodName);

    int getRole();

    void setRole(int role);

    String getDescription();

    void setDescription(String description);

    ResolvableType getResolvableType();

    boolean isSingleton();

    boolean isPrototype();

    boolean isAbstract();

    String getResourceDescription();

    org.springframework.beans.factory.config.BeanDefinition getOriginatingBeanDefinition();

    boolean hasConstructorArgumentValues();

    boolean hasPropertyValues();

    Object getSource();

    <T> T computeAttribute(String name, Function<String, T> computeFunction);
}
