package cool.scx.bean;

import org.springframework.beans.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.*;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.metrics.ApplicationStartup;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.util.StringValueResolver;

import java.beans.PropertyEditor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;

/// 暂时使用 spring 做实现
public class DefaultListableBeanFactory implements BeanFactory {

    private final org.springframework.beans.factory.support.DefaultListableBeanFactory springDefaultListableBeanFactory;

    public DefaultListableBeanFactory() {
        this.springDefaultListableBeanFactory = new org.springframework.beans.factory.support.DefaultListableBeanFactory();
    }

    @Override
    public Object getBean(String name) {
        return springDefaultListableBeanFactory.getBean(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return springDefaultListableBeanFactory.getBean(name, requiredType);
    }

    @Override
    public Object getBean(String name, Object... args) {
        return springDefaultListableBeanFactory.getBean(name, args);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        return springDefaultListableBeanFactory.getBean(requiredType);
    }

    @Override
    public <T> T getBean(Class<T> requiredType, Object... args) {
        return springDefaultListableBeanFactory.getBean(requiredType, args);
    }

    @Override
    public <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType) {
        return springDefaultListableBeanFactory.getBeanProvider(requiredType);
    }

    @Override
    public <T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType) {
        return springDefaultListableBeanFactory.getBeanProvider(requiredType);
    }

    @Override
    public boolean containsBean(String name) {
        return springDefaultListableBeanFactory.containsBean(name);
    }

    @Override
    public boolean isSingleton(String name) {
        return springDefaultListableBeanFactory.isSingleton(name);
    }

    @Override
    public boolean isPrototype(String name) {
        return springDefaultListableBeanFactory.isPrototype(name);
    }

    @Override
    public boolean isTypeMatch(String name, ResolvableType typeToMatch) {
        return springDefaultListableBeanFactory.isTypeMatch(name, typeToMatch);
    }

    @Override
    public boolean isTypeMatch(String name, Class<?> typeToMatch) {
        return springDefaultListableBeanFactory.isTypeMatch(name, typeToMatch);
    }

    @Override
    public Class<?> getType(String name) {
        return springDefaultListableBeanFactory.getType(name);
    }

    @Override
    public Class<?> getType(String name, boolean allowFactoryBeanInit) {
        return springDefaultListableBeanFactory.getType(name, allowFactoryBeanInit);
    }

    @Override
    public String[] getAliases(String name) {
        return springDefaultListableBeanFactory.getAliases(name);
    }

    public void registerBeanDefinition(String beanName, AnnotatedGenericBeanDefinition beanDefinition) {
        springDefaultListableBeanFactory.registerBeanDefinition(beanName, new AnnotatedBeanDefinition() {

            @Override
            public AnnotationMetadata getMetadata() {
                return beanDefinition.getMetadata();
            }

            @Override
            public MethodMetadata getFactoryMethodMetadata() {
                return beanDefinition.getFactoryMethodMetadata();
            }

            @Override
            public void setAttribute(String name, Object value) {
                beanDefinition.setAttribute(name, value);
            }

            @Override
            public Object getAttribute(String name) {
                return beanDefinition.getAttribute(name);
            }

            @Override
            public Object removeAttribute(String name) {
                return beanDefinition.removeAttribute(name);
            }

            @Override
            public boolean hasAttribute(String name) {
                return beanDefinition.hasAttribute(name);
            }

            @Override
            public String[] attributeNames() {
                return beanDefinition.attributeNames();
            }

            @Override
            public String getParentName() {
                return beanDefinition.getParentName();
            }

            @Override
            public void setParentName(String parentName) {
                beanDefinition.setParentName(parentName);
            }

            @Override
            public String getBeanClassName() {
                return beanDefinition.getBeanClassName();
            }

            @Override
            public void setBeanClassName(String beanClassName) {
                beanDefinition.setBeanClassName(beanClassName);
            }

            @Override
            public String getScope() {
                return beanDefinition.getScope();
            }

            @Override
            public void setScope(String scope) {
                beanDefinition.setScope(scope);
            }

            @Override
            public boolean isLazyInit() {
                return beanDefinition.isLazyInit();
            }

            @Override
            public void setLazyInit(boolean lazyInit) {
                beanDefinition.setLazyInit(lazyInit);
            }

            @Override
            public String[] getDependsOn() {
                return beanDefinition.getDependsOn();
            }

            @Override
            public void setDependsOn(String... dependsOn) {
                beanDefinition.setDependsOn(dependsOn);
            }

            @Override
            public boolean isAutowireCandidate() {
                return beanDefinition.isAutowireCandidate();
            }

            @Override
            public void setAutowireCandidate(boolean autowireCandidate) {
                beanDefinition.setAutowireCandidate(autowireCandidate);
            }

            @Override
            public boolean isPrimary() {
                return beanDefinition.isPrimary();
            }

            @Override
            public void setPrimary(boolean primary) {
                beanDefinition.setPrimary(primary);
            }

            @Override
            public boolean isFallback() {
                return beanDefinition.isFallback();
            }

            @Override
            public void setFallback(boolean fallback) {
                beanDefinition.setFallback(fallback);
            }

            @Override
            public String getFactoryBeanName() {
                return beanDefinition.getFactoryBeanName();
            }

            @Override
            public void setFactoryBeanName(String factoryBeanName) {
                beanDefinition.setFactoryBeanName(factoryBeanName);
            }

            @Override
            public String getFactoryMethodName() {
                return beanDefinition.getFactoryMethodName();
            }

            @Override
            public void setFactoryMethodName(String factoryMethodName) {
                beanDefinition.setFactoryMethodName(factoryMethodName);
            }

            @Override
            public ConstructorArgumentValues getConstructorArgumentValues() {
                return beanDefinition.getConstructorArgumentValues();
            }

            @Override
            public MutablePropertyValues getPropertyValues() {
                return beanDefinition.getPropertyValues();
            }

            @Override
            public String getInitMethodName() {
                return beanDefinition.getInitMethodName();
            }

            @Override
            public void setInitMethodName(String initMethodName) {
                beanDefinition.setInitMethodName(initMethodName);
            }

            @Override
            public String getDestroyMethodName() {
                return beanDefinition.getDestroyMethodName();
            }

            @Override
            public void setDestroyMethodName(String destroyMethodName) {
                beanDefinition.setDestroyMethodName(destroyMethodName);
            }

            @Override
            public int getRole() {
                return beanDefinition.getRole();
            }

            @Override
            public void setRole(int role) {
                beanDefinition.setRole(role);
            }

            @Override
            public String getDescription() {
                return beanDefinition.getDescription();
            }

            @Override
            public void setDescription(String description) {
                beanDefinition.setDescription(description);
            }

            @Override
            public ResolvableType getResolvableType() {
                return beanDefinition.getResolvableType();
            }

            @Override
            public boolean isSingleton() {
                return beanDefinition.isSingleton();
            }

            @Override
            public boolean isPrototype() {
                return beanDefinition.isPrototype();
            }

            @Override
            public boolean isAbstract() {
                return beanDefinition.isAbstract();
            }

            @Override
            public String getResourceDescription() {
                return beanDefinition.getResourceDescription();
            }

            @Override
            public org.springframework.beans.factory.config.BeanDefinition getOriginatingBeanDefinition() {
                return beanDefinition.getOriginatingBeanDefinition();
            }

            @Override
            public boolean hasConstructorArgumentValues() {
                return beanDefinition.hasConstructorArgumentValues();
            }

            @Override
            public boolean hasPropertyValues() {
                return beanDefinition.hasPropertyValues();
            }

            @Override
            public Object getSource() {
                return beanDefinition.getSource();
            }

            @Override
            public <T> T computeAttribute(String name, Function<String, T> computeFunction) {
                return beanDefinition.computeAttribute(name, computeFunction);
            }
        });
    }

    public String[] getBeanDefinitionNames() {
        return springDefaultListableBeanFactory.getBeanDefinitionNames();
    }

    public void preInstantiateSingletons() {
        springDefaultListableBeanFactory.preInstantiateSingletons();
    }

    public void addBeanPostProcessor(cool.scx.bean.AutowiredAnnotationBeanPostProcessor beanPostProcessor) {
        springDefaultListableBeanFactory.addBeanPostProcessor(new org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor() {

            @Override
            public Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
                return beanPostProcessor.predictBeanType(beanClass, beanName);
            }

            @Override
            public Class<?> determineBeanType(Class<?> beanClass, String beanName) throws BeansException {
                return beanPostProcessor.determineBeanType(beanClass, beanName);
            }

            @Override
            public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeansException {
                return beanPostProcessor.determineCandidateConstructors(beanClass, beanName);
            }

            @Override
            public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
                return beanPostProcessor.getEarlyBeanReference(bean, beanName);
            }

            @Override
            public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
                return beanPostProcessor.postProcessBeforeInstantiation(beanClass, beanName);
            }

            @Override
            public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
                return beanPostProcessor.postProcessAfterInstantiation(bean, beanName);
            }

            @Override
            public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
                return beanPostProcessor.postProcessProperties(pvs, bean, beanName);
            }

            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                return beanPostProcessor.postProcessBeforeInitialization(bean, beanName);
            }

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                return beanPostProcessor.postProcessAfterInitialization(bean, beanName);
            }

        });
    }

    public void setAllowCircularReferences(boolean aBoolean) {
        springDefaultListableBeanFactory.setAllowCircularReferences(aBoolean);
    }

    public void registerSingleton(String beanName, Object singletonObject) {
        springDefaultListableBeanFactory.registerSingleton(beanName, singletonObject);
    }

    public void addSingletonCallback(String beanName, Consumer<Object> singletonConsumer) {
        springDefaultListableBeanFactory.addSingletonCallback(beanName, singletonConsumer);
    }

    public Object getSingleton(String beanName) {
        return springDefaultListableBeanFactory.getSingleton(beanName);
    }

    public boolean containsSingleton(String beanName) {
        return springDefaultListableBeanFactory.containsSingleton(beanName);
    }

    public String[] getSingletonNames() {
        return springDefaultListableBeanFactory.getSingletonNames();
    }

    public int getSingletonCount() {
        return springDefaultListableBeanFactory.getSingletonCount();
    }

    public Object getSingletonMutex() {
        return springDefaultListableBeanFactory.getSingletonMutex();
    }

    public ClassLoader getBeanClassLoader() {
        return springDefaultListableBeanFactory.getBeanClassLoader();
    }

    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        springDefaultListableBeanFactory.setBeanClassLoader(beanClassLoader);
    }

    public ClassLoader getTempClassLoader() {
        return springDefaultListableBeanFactory.getTempClassLoader();
    }

    public void setTempClassLoader(ClassLoader tempClassLoader) {
        springDefaultListableBeanFactory.setTempClassLoader(tempClassLoader);
    }

    public boolean isCacheBeanMetadata() {
        return springDefaultListableBeanFactory.isCacheBeanMetadata();
    }

    public void setCacheBeanMetadata(boolean cacheBeanMetadata) {
        springDefaultListableBeanFactory.setCacheBeanMetadata(cacheBeanMetadata);
    }

    public BeanExpressionResolver getBeanExpressionResolver() {
        return springDefaultListableBeanFactory.getBeanExpressionResolver();
    }

    public void setBeanExpressionResolver(BeanExpressionResolver resolver) {
        springDefaultListableBeanFactory.setBeanExpressionResolver(resolver);
    }

    public Executor getBootstrapExecutor() {
        return springDefaultListableBeanFactory.getBootstrapExecutor();
    }

    public void setBootstrapExecutor(Executor executor) {
        springDefaultListableBeanFactory.setBootstrapExecutor(executor);
    }

    public Object initializeBean(Object existingBean, String beanName) {
        return springDefaultListableBeanFactory.initializeBean(existingBean, beanName);
    }

    public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
        return springDefaultListableBeanFactory.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
    }

    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        return springDefaultListableBeanFactory.getBeansOfType(type);

    }

    public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) {
        return springDefaultListableBeanFactory.getBeansOfType(type, includeNonSingletons, allowEagerInit);
    }

    public String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) {
        return springDefaultListableBeanFactory.getBeanNamesForAnnotation(annotationType);
    }

    public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
        return springDefaultListableBeanFactory.getBeansWithAnnotation(annotationType);
    }

    public org.springframework.beans.factory.BeanFactory getParentBeanFactory() {
        return springDefaultListableBeanFactory.getParentBeanFactory();
    }

    public void setParentBeanFactory(org.springframework.beans.factory.BeanFactory parentBeanFactory) {
        springDefaultListableBeanFactory.setParentBeanFactory(parentBeanFactory);
    }

    public boolean containsLocalBean(String name) {
        return springDefaultListableBeanFactory.containsLocalBean(name);
    }

    public void registerResolvableDependency(Class<?> dependencyType, Object autowiredValue) {

        springDefaultListableBeanFactory.registerResolvableDependency(dependencyType, autowiredValue);
    }

    public org.springframework.beans.factory.config.BeanDefinition getBeanDefinition(String beanName) {

        return springDefaultListableBeanFactory.getBeanDefinition(beanName);
    }

    public Iterator<String> getBeanNamesIterator() {
        return springDefaultListableBeanFactory.getBeanNamesIterator();

    }

    public void clearMetadataCache() {

        springDefaultListableBeanFactory.clearMetadataCache();

    }

    public void freezeConfiguration() {
        springDefaultListableBeanFactory.freezeConfiguration();

    }

    public boolean isConfigurationFrozen() {
        return springDefaultListableBeanFactory.isConfigurationFrozen();
    }

    public boolean isAutowireCandidate(String beanName, DependencyDescriptor descriptor) {
        return springDefaultListableBeanFactory.isAutowireCandidate(beanName, descriptor);
    }

    public void ignoreDependencyInterface(Class<?> ifc) {
        springDefaultListableBeanFactory.ignoreDependencyInterface(ifc);
    }

    public void ignoreDependencyType(Class<?> type) {
        springDefaultListableBeanFactory.ignoreDependencyType(type);
    }

    public <A extends Annotation> Set<A> findAllAnnotationsOnBean(String beanName, Class<A> annotationType, boolean allowFactoryBeanInit) {

        return springDefaultListableBeanFactory.findAllAnnotationsOnBean(beanName, annotationType, allowFactoryBeanInit);
    }

    public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType, boolean allowFactoryBeanInit) {
        return springDefaultListableBeanFactory.findAnnotationOnBean(beanName, annotationType, allowFactoryBeanInit);
    }

    public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) {
        return springDefaultListableBeanFactory.findAnnotationOnBean(beanName, annotationType);
    }

    public <T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType, boolean allowEagerInit) {
        return springDefaultListableBeanFactory.getBeanProvider(requiredType, allowEagerInit);
    }

    public <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType, boolean allowEagerInit) {
        return springDefaultListableBeanFactory.getBeanProvider(requiredType, allowEagerInit);
    }

    public String[] getBeanNamesForType(ResolvableType type) {
        return springDefaultListableBeanFactory.getBeanNamesForType(type);
    }

    public int getBeanDefinitionCount() {
        return springDefaultListableBeanFactory.getBeanDefinitionCount();
    }

    public boolean containsBeanDefinition(String beanName) {
        return springDefaultListableBeanFactory.containsBeanDefinition(beanName);
    }

    public Object resolveDependency(DependencyDescriptor descriptor, String requestingBeanName, Set<String> autowiredBeanNames, TypeConverter typeConverter) {
        return springDefaultListableBeanFactory.resolveDependency(descriptor, requestingBeanName, autowiredBeanNames, typeConverter);
    }

    public Object resolveDependency(DependencyDescriptor descriptor, String requestingBeanName) {
        return springDefaultListableBeanFactory.resolveDependency(descriptor, requestingBeanName);
    }

    public Object resolveBeanByName(String name, DependencyDescriptor descriptor) {
        return springDefaultListableBeanFactory.getBean(name, descriptor);
    }

    public <T> NamedBeanHolder<T> resolveNamedBean(Class<T> requiredType) {
        return springDefaultListableBeanFactory.resolveNamedBean(requiredType);
    }

    public void destroyBean(Object existingBean) {
        springDefaultListableBeanFactory.destroyBean(existingBean);
    }

    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) {
        return springDefaultListableBeanFactory.applyBeanPostProcessorsAfterInitialization(existingBean, beanName);
    }

    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) {
        return springDefaultListableBeanFactory.applyBeanPostProcessorsAfterInitialization(existingBean, beanName);
    }

    public void applyBeanPropertyValues(Object existingBean, String beanName) {
        springDefaultListableBeanFactory.applyBeanPropertyValues(existingBean, beanName);
    }

    public void autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck) {
        springDefaultListableBeanFactory.autowireBeanProperties(existingBean, autowireMode, dependencyCheck);
    }

    public Object autowire(Class<?> beanClass, int autowireMode, boolean dependencyCheck) {
        return springDefaultListableBeanFactory.autowire(beanClass, autowireMode, dependencyCheck);
    }

    public Object createBean(Class<?> beanClass, int autowireMode, boolean dependencyCheck) {
        return springDefaultListableBeanFactory.createBean(beanClass, autowireMode, dependencyCheck);
    }

    public Object configureBean(Object existingBean, String beanName) {
        return springDefaultListableBeanFactory.configureBean(existingBean, beanName);
    }

    public void autowireBean(Object existingBean) {
        springDefaultListableBeanFactory.autowireBean(existingBean);
    }

    public <T> T createBean(Class<T> beanClass) {
        return springDefaultListableBeanFactory.createBean(beanClass);
    }

    public void destroySingletons() {
        springDefaultListableBeanFactory.destroySingletons();
    }

    public void destroyScopedBean(String beanName) {
        springDefaultListableBeanFactory.destroyScopedBean(beanName);
    }

    public void destroyBean(String beanName, Object beanInstance) {
        springDefaultListableBeanFactory.destroyBean(beanName, beanInstance);
    }

    public String[] getDependenciesForBean(String beanName) {
        return springDefaultListableBeanFactory.getDependenciesForBean(beanName);
    }

    public String[] getDependentBeans(String beanName) {
        return springDefaultListableBeanFactory.getDependentBeans(beanName);
    }

    public void registerDependentBean(String beanName, String dependentBeanName) {
        springDefaultListableBeanFactory.registerDependentBean(beanName, dependentBeanName);
    }

    public boolean isCurrentlyInCreation(String beanName) {
        return springDefaultListableBeanFactory.isCurrentlyInCreation(beanName);
    }

    public void setCurrentlyInCreation(String beanName, boolean inCreation) {
        springDefaultListableBeanFactory.setCurrentlyInCreation(beanName, inCreation);
    }

    public boolean isFactoryBean(String name) {
        return springDefaultListableBeanFactory.isFactoryBean(name);
    }

    public org.springframework.beans.factory.config.BeanDefinition getMergedBeanDefinition(String beanName) {
        return springDefaultListableBeanFactory.getMergedBeanDefinition(beanName);
    }

    public void resolveAliases(StringValueResolver valueResolver) {
        springDefaultListableBeanFactory.resolveAliases(valueResolver);
    }

    public void registerAlias(String beanName, String alias) {
        springDefaultListableBeanFactory.registerAlias(beanName, alias);
    }

    public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory) {
        springDefaultListableBeanFactory.copyConfigurationFrom(otherFactory);
    }

    public ApplicationStartup getApplicationStartup() {
        return springDefaultListableBeanFactory.getApplicationStartup();
    }

    public void setApplicationStartup(ApplicationStartup applicationStartup) {
        springDefaultListableBeanFactory.setApplicationStartup(applicationStartup);
    }

    public Scope getRegisteredScope(String scopeName) {
        return springDefaultListableBeanFactory.getRegisteredScope(scopeName);
    }

    public String[] getRegisteredScopeNames() {
        return springDefaultListableBeanFactory.getRegisteredScopeNames();
    }

    public void registerScope(String scopeName, Scope scope) {
        springDefaultListableBeanFactory.registerScope(scopeName, scope);
    }

    public int getBeanPostProcessorCount() {
        return springDefaultListableBeanFactory.getBeanPostProcessorCount();
    }

    public String resolveEmbeddedValue(String value) {
        return springDefaultListableBeanFactory.resolveEmbeddedValue(value);
    }

    public boolean hasEmbeddedValueResolver() {
        return springDefaultListableBeanFactory.hasEmbeddedValueResolver();
    }

    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
        springDefaultListableBeanFactory.addEmbeddedValueResolver(valueResolver);
    }

    public TypeConverter getTypeConverter() {
        return springDefaultListableBeanFactory.getTypeConverter();
    }

    public void setTypeConverter(TypeConverter typeConverter) {
        springDefaultListableBeanFactory.setTypeConverter(typeConverter);
    }

    public void copyRegisteredEditorsTo(PropertyEditorRegistry registry) {
        springDefaultListableBeanFactory.copyRegisteredEditorsTo(registry);
    }

    public void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass) {
        springDefaultListableBeanFactory.registerCustomEditor(requiredType, propertyEditorClass);
    }

    public void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar) {
        springDefaultListableBeanFactory.addPropertyEditorRegistrar(registrar);
    }

    public ConversionService getConversionService() {
        return springDefaultListableBeanFactory.getConversionService();
    }

    public void setConversionService(ConversionService conversionService) {
        springDefaultListableBeanFactory.setConversionService(conversionService);
    }

    public String[] getBeanNamesForType(Class<?> type) {
        return springDefaultListableBeanFactory.getBeanNamesForType(type);
    }

    public String[] getBeanNamesForType(ResolvableType type, boolean includeNonSingletons, boolean allowEagerInit) {
        return springDefaultListableBeanFactory.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
    }

}
