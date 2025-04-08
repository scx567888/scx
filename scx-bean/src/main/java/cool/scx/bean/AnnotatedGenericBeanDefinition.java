package cool.scx.bean;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.core.ResolvableType;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;

import java.util.function.Function;

public class AnnotatedGenericBeanDefinition implements BeanDefinition {

    private final org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition springAnnotatedGenericBeanDefinition;

    public AnnotatedGenericBeanDefinition(Class<?> beanClass) {
        this.springAnnotatedGenericBeanDefinition = new org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition(beanClass);
    }

    @Override
    public void setAttribute(String name, Object value) {
        springAnnotatedGenericBeanDefinition.setAttribute(name, value);
    }

    @Override
    public Object getAttribute(String name) {
        return springAnnotatedGenericBeanDefinition.getAttribute(name);
    }

    @Override
    public Object removeAttribute(String name) {
        return springAnnotatedGenericBeanDefinition.removeAttribute(name);
    }

    @Override
    public boolean hasAttribute(String name) {
        return springAnnotatedGenericBeanDefinition.hasAttribute(name);
    }

    @Override
    public String[] attributeNames() {
        return springAnnotatedGenericBeanDefinition.attributeNames();
    }

    @Override
    public void setParentName(String parentName) {
        springAnnotatedGenericBeanDefinition.setParentName(parentName);
    }

    @Override
    public String getParentName() {
        return springAnnotatedGenericBeanDefinition.getParentName();
    }

    @Override
    public void setBeanClassName(String beanClassName) {
        springAnnotatedGenericBeanDefinition.setBeanClassName(beanClassName);
    }

    @Override
    public String getBeanClassName() {
        return springAnnotatedGenericBeanDefinition.getBeanClassName();
    }

    @Override
    public void setScope(String scope) {
        springAnnotatedGenericBeanDefinition.setScope(scope);
    }

    @Override
    public String getScope() {
        return springAnnotatedGenericBeanDefinition.getScope();
    }

    @Override
    public void setLazyInit(boolean lazyInit) {
        springAnnotatedGenericBeanDefinition.setLazyInit(lazyInit);
    }

    @Override
    public boolean isLazyInit() {
        return springAnnotatedGenericBeanDefinition.isLazyInit();
    }

    @Override
    public void setDependsOn(String[] dependsOn) {
        springAnnotatedGenericBeanDefinition.setDependsOn(dependsOn);
    }

    @Override
    public String[] getDependsOn() {
        return springAnnotatedGenericBeanDefinition.getDependsOn();
    }

    @Override
    public void setAutowireCandidate(boolean autowireCandidate) {
        springAnnotatedGenericBeanDefinition.setAutowireCandidate(autowireCandidate);
    }

    @Override
    public boolean isAutowireCandidate() {
        return springAnnotatedGenericBeanDefinition.isAutowireCandidate();
    }

    @Override
    public void setPrimary(boolean primary) {
        springAnnotatedGenericBeanDefinition.setPrimary(primary);
    }

    @Override
    public boolean isPrimary() {
        return springAnnotatedGenericBeanDefinition.isPrimary();
    }

    @Override
    public void setFallback(boolean fallback) {
        springAnnotatedGenericBeanDefinition.setFallback(fallback);
    }

    @Override
    public boolean isFallback() {
        return springAnnotatedGenericBeanDefinition.isFallback();
    }

    @Override
    public void setFactoryBeanName(String factoryBeanName) {
        springAnnotatedGenericBeanDefinition.setFactoryBeanName(factoryBeanName);
    }

    @Override
    public String getFactoryBeanName() {
        return springAnnotatedGenericBeanDefinition.getFactoryBeanName();
    }

    @Override
    public void setFactoryMethodName(String factoryMethodName) {
        springAnnotatedGenericBeanDefinition.setFactoryMethodName(factoryMethodName);
    }

    @Override
    public String getFactoryMethodName() {
        return springAnnotatedGenericBeanDefinition.getFactoryMethodName();
    }

    @Override
    public ConstructorArgumentValues getConstructorArgumentValues() {
        return springAnnotatedGenericBeanDefinition.getConstructorArgumentValues();
    }

    @Override
    public MutablePropertyValues getPropertyValues() {
        return springAnnotatedGenericBeanDefinition.getPropertyValues();
    }

    @Override
    public void setInitMethodName(String initMethodName) {
        springAnnotatedGenericBeanDefinition.setInitMethodName(initMethodName);
    }

    @Override
    public String getInitMethodName() {
        return springAnnotatedGenericBeanDefinition.getInitMethodName();
    }

    @Override
    public void setDestroyMethodName(String destroyMethodName) {
        springAnnotatedGenericBeanDefinition.setDestroyMethodName(destroyMethodName);
    }

    @Override
    public String getDestroyMethodName() {
        return springAnnotatedGenericBeanDefinition.getDestroyMethodName();
    }

    @Override
    public void setRole(int role) {
        springAnnotatedGenericBeanDefinition.setRole(role);
    }

    @Override
    public int getRole() {
        return springAnnotatedGenericBeanDefinition.getRole();
    }

    @Override
    public void setDescription(String description) {
        springAnnotatedGenericBeanDefinition.setDescription(description);
    }

    @Override
    public String getDescription() {
        return springAnnotatedGenericBeanDefinition.getDescription();
    }

    @Override
    public ResolvableType getResolvableType() {
        return springAnnotatedGenericBeanDefinition.getResolvableType();
    }

    @Override
    public boolean isSingleton() {
        return springAnnotatedGenericBeanDefinition.isSingleton();
    }

    @Override
    public boolean isPrototype() {
        return springAnnotatedGenericBeanDefinition.isPrototype();
    }

    @Override
    public boolean isAbstract() {
        return springAnnotatedGenericBeanDefinition.isAbstract();
    }

    @Override
    public String getResourceDescription() {
        return springAnnotatedGenericBeanDefinition.getResourceDescription();
    }

    @Override
    public org.springframework.beans.factory.config.BeanDefinition getOriginatingBeanDefinition() {
        return springAnnotatedGenericBeanDefinition.getOriginatingBeanDefinition();
    }

    @Override
    public boolean hasConstructorArgumentValues() {
        return springAnnotatedGenericBeanDefinition.hasConstructorArgumentValues();
    }

    @Override
    public boolean hasPropertyValues() {
        return springAnnotatedGenericBeanDefinition.hasPropertyValues();
    }

    @Override
    public Object getSource() {
        return springAnnotatedGenericBeanDefinition.getSource();
    }

    @Override
    public <T> T computeAttribute(String name, Function<String, T> computeFunction) {
        return springAnnotatedGenericBeanDefinition.computeAttribute(name, computeFunction);
    }

    public AnnotationMetadata getMetadata() {
        return springAnnotatedGenericBeanDefinition.getMetadata();
    }

    public MethodMetadata getFactoryMethodMetadata() {
        return springAnnotatedGenericBeanDefinition.getFactoryMethodMetadata();
    }
    
}
