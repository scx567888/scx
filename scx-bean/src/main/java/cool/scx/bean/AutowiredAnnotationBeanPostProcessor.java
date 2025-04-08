package cool.scx.bean;

import org.springframework.beans.*;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.metrics.ApplicationStartup;
import org.springframework.util.StringValueResolver;

import java.beans.PropertyEditor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {

    private final org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor springAutowiredAnnotationBeanPostProcessor;

    public AutowiredAnnotationBeanPostProcessor() {
        this.springAutowiredAnnotationBeanPostProcessor = new org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return springAutowiredAnnotationBeanPostProcessor.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return springAutowiredAnnotationBeanPostProcessor.postProcessAfterInitialization(bean, beanName);
    }

    public void setBeanFactory(DefaultListableBeanFactory beanFactory) {
        springAutowiredAnnotationBeanPostProcessor.setBeanFactory(new ConfigurableListableBeanFactory() {
            
            @Override
            public void registerSingleton(String beanName, Object singletonObject) {
                beanFactory.registerSingleton(beanName,singletonObject);
            }

            @Override
            public void addSingletonCallback(String beanName, Consumer<Object> singletonConsumer) {
                beanFactory.addSingletonCallback(beanName,singletonConsumer);
            }

            @Override
            public Object getSingleton(String beanName) {
                return beanFactory.getSingleton(beanName);
            }

            @Override
            public boolean containsSingleton(String beanName) {
                return beanFactory.containsSingleton(beanName);
            }

            @Override
            public String[] getSingletonNames() {
                return beanFactory.getSingletonNames();
            }

            @Override
            public int getSingletonCount() {
                return beanFactory.getSingletonCount();
            }

            @Override
            public Object getSingletonMutex() {
                return beanFactory.getSingletonMutex();
            }

            @Override
            public void setParentBeanFactory(org.springframework.beans.factory.BeanFactory parentBeanFactory) throws IllegalStateException {
                beanFactory.setParentBeanFactory(parentBeanFactory);
            }

            @Override
            public void setBeanClassLoader(ClassLoader beanClassLoader) {
                beanFactory.setBeanClassLoader(beanClassLoader);
            }

            @Override
            public ClassLoader getBeanClassLoader() {
                return beanFactory.getBeanClassLoader();
            }

            @Override
            public void setTempClassLoader(ClassLoader tempClassLoader) {
                beanFactory.setTempClassLoader(tempClassLoader);
            }

            @Override
            public ClassLoader getTempClassLoader() {
                return beanFactory.getTempClassLoader();
            }

            @Override
            public void setCacheBeanMetadata(boolean cacheBeanMetadata) {
                beanFactory.setCacheBeanMetadata(cacheBeanMetadata);
            }

            @Override
            public boolean isCacheBeanMetadata() {
                return beanFactory.isCacheBeanMetadata();
            }

            @Override
            public void setBeanExpressionResolver(BeanExpressionResolver resolver) {
                beanFactory.setBeanExpressionResolver(resolver);
            }

            @Override
            public BeanExpressionResolver getBeanExpressionResolver() {
                return beanFactory.getBeanExpressionResolver();
            }

            @Override
            public void setBootstrapExecutor(Executor executor) {
                beanFactory.setBootstrapExecutor(executor);
            }

            @Override
            public Executor getBootstrapExecutor() {
                return beanFactory.getBootstrapExecutor();
            }

            @Override
            public void setConversionService(ConversionService conversionService) {
                beanFactory.setConversionService(conversionService);
            }

            @Override
            public ConversionService getConversionService() {
                return beanFactory.getConversionService();
            }

            @Override
            public void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar) {
                beanFactory.addPropertyEditorRegistrar(registrar);
            }

            @Override
            public void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass) {
                beanFactory.registerCustomEditor(requiredType,propertyEditorClass);
            }

            @Override
            public void copyRegisteredEditorsTo(PropertyEditorRegistry registry) {
                beanFactory.copyRegisteredEditorsTo(registry);
            }

            @Override
            public void setTypeConverter(TypeConverter typeConverter) {
                beanFactory.setTypeConverter(typeConverter);
            }

            @Override
            public TypeConverter getTypeConverter() {
                return beanFactory.getTypeConverter();
            }

            @Override
            public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
                beanFactory.addEmbeddedValueResolver(valueResolver);
            }

            @Override
            public boolean hasEmbeddedValueResolver() {
                return beanFactory.hasEmbeddedValueResolver();
            }

            @Override
            public String resolveEmbeddedValue(String value) {
                return beanFactory.resolveEmbeddedValue(value);
            }

            @Override
            public void addBeanPostProcessor(org.springframework.beans.factory.config.BeanPostProcessor beanPostProcessor) {
                throw new UnsupportedOperationException("addBeanPostProcessor not supported");
            }

            @Override
            public int getBeanPostProcessorCount() {
                return beanFactory.getBeanPostProcessorCount();
            }

            @Override
            public void registerScope(String scopeName, Scope scope) {
                beanFactory.registerScope(scopeName,scope);
            }

            @Override
            public String[] getRegisteredScopeNames() {
                return beanFactory.getRegisteredScopeNames();
            }

            @Override
            public Scope getRegisteredScope(String scopeName) {
                return beanFactory.getRegisteredScope(scopeName);
            }

            @Override
            public void setApplicationStartup(ApplicationStartup applicationStartup) {
                beanFactory.setApplicationStartup(applicationStartup);
            }

            @Override
            public ApplicationStartup getApplicationStartup() {
                return beanFactory.getApplicationStartup();
            }

            @Override
            public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory) {
                beanFactory.copyConfigurationFrom(otherFactory);
            }

            @Override
            public void registerAlias(String beanName, String alias) throws BeanDefinitionStoreException {
                beanFactory.registerAlias(beanName,alias);
            }

            @Override
            public void resolveAliases(StringValueResolver valueResolver) {
                beanFactory.resolveAliases(valueResolver);
            }

            @Override
            public BeanDefinition getMergedBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
                return beanFactory.getMergedBeanDefinition(beanName);
            }

            @Override
            public boolean isFactoryBean(String name) throws NoSuchBeanDefinitionException {
                return beanFactory.isFactoryBean(name);
            }

            @Override
            public void setCurrentlyInCreation(String beanName, boolean inCreation) {
                beanFactory.setCurrentlyInCreation(beanName,inCreation);
            }

            @Override
            public boolean isCurrentlyInCreation(String beanName) {
                return beanFactory.isCurrentlyInCreation(beanName);
            }

            @Override
            public void registerDependentBean(String beanName, String dependentBeanName) {
                beanFactory.registerDependentBean(beanName,dependentBeanName);
            }

            @Override
            public String[] getDependentBeans(String beanName) {
                return beanFactory.getDependentBeans(beanName);
            }

            @Override
            public String[] getDependenciesForBean(String beanName) {
                return beanFactory.getDependenciesForBean(beanName);
            }

            @Override
            public void destroyBean(String beanName, Object beanInstance) {
                beanFactory.destroyBean(beanName,beanInstance);
            }

            @Override
            public void destroyScopedBean(String beanName) {
                beanFactory.destroyScopedBean(beanName);
            }

            @Override
            public void destroySingletons() {
                beanFactory.destroySingletons();
            }

            @Override
            public <T> T createBean(Class<T> beanClass) throws BeansException {
                return beanFactory.createBean(beanClass);
            }

            @Override
            public void autowireBean(Object existingBean) throws BeansException {
                beanFactory.autowireBean(existingBean);
            }

            @Override
            public Object configureBean(Object existingBean, String beanName) throws BeansException {
                return beanFactory.configureBean(existingBean,beanName);
            }

            @Override
            public Object createBean(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException {
                return beanFactory.createBean(beanClass,autowireMode,dependencyCheck);
            }

            @Override
            public Object autowire(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException {
                return beanFactory.autowire(beanClass,autowireMode,dependencyCheck);
            }

            @Override
            public void autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck) throws BeansException {
                beanFactory.autowireBeanProperties(existingBean,autowireMode,dependencyCheck);
            }

            @Override
            public void applyBeanPropertyValues(Object existingBean, String beanName) throws BeansException {
                beanFactory.applyBeanPropertyValues(existingBean,beanName);
            }

            @Override
            public Object initializeBean(Object existingBean, String beanName) throws BeansException {
                return beanFactory.initializeBean(existingBean,beanName);
            }

            @Override
            public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
                return beanFactory.applyBeanPostProcessorsBeforeInitialization(existingBean,beanName);
            }

            @Override
            public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
                return beanFactory.applyBeanPostProcessorsAfterInitialization(existingBean,beanName);
            }

            @Override
            public void destroyBean(Object existingBean) {
                beanFactory.destroyBean(existingBean);
            }

            @Override
            public <T> NamedBeanHolder<T> resolveNamedBean(Class<T> requiredType) throws BeansException {
                return beanFactory.resolveNamedBean(requiredType);
            }

            @Override
            public Object resolveBeanByName(String name, DependencyDescriptor descriptor) throws BeansException {
                return beanFactory.resolveBeanByName(name,descriptor);
            }

            @Override
            public Object resolveDependency(DependencyDescriptor descriptor, String requestingBeanName) throws BeansException {
                return beanFactory.resolveDependency(descriptor,requestingBeanName);
            }

            @Override
            public Object resolveDependency(DependencyDescriptor descriptor, String requestingBeanName, Set<String> autowiredBeanNames, TypeConverter typeConverter) throws BeansException {
                return beanFactory.resolveDependency(descriptor,requestingBeanName,autowiredBeanNames,typeConverter);
            }

            @Override
            public boolean containsBeanDefinition(String beanName) {
                return beanFactory.containsBeanDefinition(beanName);
            }

            @Override
            public int getBeanDefinitionCount() {
                return beanFactory.getBeanDefinitionCount();
            }

            @Override
            public String[] getBeanDefinitionNames() {
                return beanFactory.getBeanDefinitionNames();
            }

            @Override
            public <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType, boolean allowEagerInit) {
                return beanFactory.getBeanProvider(requiredType,allowEagerInit);
            }

            @Override
            public <T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType, boolean allowEagerInit) {
                return beanFactory.getBeanProvider(requiredType,allowEagerInit);
            }

            @Override
            public String[] getBeanNamesForType(ResolvableType type) {
                return beanFactory.getBeanNamesForType(type);
            }

            @Override
            public String[] getBeanNamesForType(ResolvableType type, boolean includeNonSingletons, boolean allowEagerInit) {
                return beanFactory.getBeanNamesForType(type,includeNonSingletons,allowEagerInit);
            }

            @Override
            public String[] getBeanNamesForType(Class<?> type) {
                return beanFactory.getBeanNamesForType(type);
            }

            @Override
            public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
                return beanFactory.getBeanNamesForType(type,includeNonSingletons,allowEagerInit);
            }

            @Override
            public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
                return beanFactory.getBeansOfType(type);
            }

            @Override
            public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException {
                return beanFactory.getBeansOfType(type,includeNonSingletons,allowEagerInit);
            }

            @Override
            public String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) {
                return beanFactory.getBeanNamesForAnnotation(annotationType);
            }

            @Override
            public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws BeansException {
                return beanFactory.getBeansWithAnnotation(annotationType);
            }

            @Override
            public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) throws NoSuchBeanDefinitionException {
                return beanFactory.findAnnotationOnBean(beanName,annotationType);
            }

            @Override
            public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType, boolean allowFactoryBeanInit) throws NoSuchBeanDefinitionException {
                return beanFactory.findAnnotationOnBean(beanName,annotationType,allowFactoryBeanInit);
            }

            @Override
            public <A extends Annotation> Set<A> findAllAnnotationsOnBean(String beanName, Class<A> annotationType, boolean allowFactoryBeanInit) throws NoSuchBeanDefinitionException {
                return beanFactory.findAllAnnotationsOnBean(beanName,annotationType,allowFactoryBeanInit);
            }

            @Override
            public org.springframework.beans.factory.BeanFactory getParentBeanFactory() {
                return beanFactory.getParentBeanFactory();
            }

            @Override
            public boolean containsLocalBean(String name) {
                return beanFactory.containsLocalBean(name);
            }

            @Override
            public void ignoreDependencyType(Class<?> type) {
                beanFactory.ignoreDependencyType(type);
            }

            @Override
            public void ignoreDependencyInterface(Class<?> ifc) {
                beanFactory.ignoreDependencyInterface(ifc);
            }

            @Override
            public void registerResolvableDependency(Class<?> dependencyType, Object autowiredValue) {
                beanFactory.registerResolvableDependency(dependencyType,autowiredValue);
            }

            @Override
            public boolean isAutowireCandidate(String beanName, DependencyDescriptor descriptor) throws NoSuchBeanDefinitionException {
                return beanFactory.isAutowireCandidate(beanName,descriptor);
            }

            @Override
            public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
                return beanFactory.getBeanDefinition(beanName);
            }

            @Override
            public Iterator<String> getBeanNamesIterator() {
                return beanFactory.getBeanNamesIterator();
            }

            @Override
            public void clearMetadataCache() {
                beanFactory.clearMetadataCache();
            }

            @Override
            public void freezeConfiguration() {
                beanFactory.freezeConfiguration();
            }

            @Override
            public boolean isConfigurationFrozen() {
                return beanFactory.isConfigurationFrozen();
            }

            @Override
            public void preInstantiateSingletons() throws BeansException {
                beanFactory.preInstantiateSingletons();
            }

            @Override
            public Object getBean(String name) throws BeansException {
                return beanFactory.getBean(name);
            }

            @Override
            public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
                return beanFactory.getBean(name, requiredType);
            }

            @Override
            public Object getBean(String name, Object... args) throws BeansException {
                return beanFactory.getBean(name, args);
            }

            @Override
            public <T> T getBean(Class<T> requiredType) throws BeansException {
                return beanFactory.getBean(requiredType);
            }

            @Override
            public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
                return beanFactory.getBean(requiredType, args);
            }

            @Override
            public <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType) {
                return beanFactory.getBeanProvider(requiredType);
            }

            @Override
            public <T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType) {
                return beanFactory.getBeanProvider(requiredType);
            }

            @Override
            public boolean containsBean(String name) {
                return beanFactory.containsBean(name);
            }

            @Override
            public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
                return beanFactory.isSingleton(name);
            }

            @Override
            public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
                return beanFactory.isPrototype(name);
            }

            @Override
            public boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
                return beanFactory.isTypeMatch(name, typeToMatch);
            }

            @Override
            public boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
                return beanFactory.isTypeMatch(name, typeToMatch);
            }

            @Override
            public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
                return beanFactory.getType(name);
            }

            @Override
            public Class<?> getType(String name, boolean allowFactoryBeanInit) throws NoSuchBeanDefinitionException {
                return beanFactory.getType(name, allowFactoryBeanInit);
            }

            @Override
            public String[] getAliases(String name) {
                return beanFactory.getAliases(name);
            }
        });
    }

    public Class<?> predictBeanType(Class<?> beanClass, String beanName) {
        return springAutowiredAnnotationBeanPostProcessor.predictBeanType(beanClass,beanName);
    }

    public Class<?> determineBeanType(Class<?> beanClass, String beanName) {
        return springAutowiredAnnotationBeanPostProcessor.determineBeanType(beanClass,beanName);
    }

    public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) {
        return springAutowiredAnnotationBeanPostProcessor.determineCandidateConstructors(beanClass,beanName);
    }

    public Object getEarlyBeanReference(Object bean, String beanName) {
        return springAutowiredAnnotationBeanPostProcessor.getEarlyBeanReference(bean,beanName);
    }

    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
        return springAutowiredAnnotationBeanPostProcessor.postProcessBeforeInstantiation(beanClass,beanName);
    }

    public boolean postProcessAfterInstantiation(Object bean, String beanName) {
        return springAutowiredAnnotationBeanPostProcessor.postProcessAfterInstantiation(bean,beanName);
    }

    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) {
        return springAutowiredAnnotationBeanPostProcessor.postProcessProperties(pvs,bean,beanName);
    }
    
}
