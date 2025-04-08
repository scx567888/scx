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

    void setParentName(String parentName);

    String getParentName();

    void setBeanClassName(String beanClassName);

    String getBeanClassName();

    void setScope(String scope);

    String getScope();

    void setLazyInit(boolean lazyInit);

    boolean isLazyInit();

    void setDependsOn(String[] dependsOn);

    String[] getDependsOn();

    void setAutowireCandidate(boolean autowireCandidate);

    boolean isAutowireCandidate();

    void setPrimary(boolean primary);

    boolean isPrimary();
    
    void setFallback(boolean fallback);

    boolean isFallback();

    void setFactoryBeanName(String factoryBeanName);

    String getFactoryBeanName();

    void setFactoryMethodName(String factoryMethodName);

    String getFactoryMethodName();

    ConstructorArgumentValues getConstructorArgumentValues();

    MutablePropertyValues getPropertyValues();

    void setInitMethodName(String initMethodName);

    String getInitMethodName();

    void setDestroyMethodName(String destroyMethodName);

    String getDestroyMethodName();

    void setRole(int role);

    int getRole();

    void setDescription(String description);

    String getDescription();

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
