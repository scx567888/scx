package cool.scx.bean;

import cool.scx.bean.exception.*;
import cool.scx.bean.provider.*;
import cool.scx.bean.resolver.BeanResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class BeanFactoryImpl implements BeanFactory {

    private final Map<String, BeanProvider> beanProviderMap = new ConcurrentHashMap<>();
    private final List<BeanResolver> beanResolvers = new ArrayList<>();

    @Override
    public Object getBean(String name) throws NoSuchBeanException {
        return findBeanProvider(name).getBean(this);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws NoSuchBeanException, NoUniqueBeanException {
        return (T) findBeanProvider(requiredType).getBean(this);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws NoSuchBeanException {
        return (T) findBeanProvider(name, requiredType).getBean(this);
    }

    @Override
    public void registerBean(String name, Object bean, boolean injecting) {
        if (injecting) {
            registerBeanProvider(name, new InjectingBeanProvider(new InstanceBeanProvider(bean)));
        } else {
            registerBeanProvider(name, new InstanceBeanProvider(bean));
        }
    }

    @Override
    public void registerBeanClass(String name, Class<?> beanClass, boolean singleton) throws DuplicateBeanNameException, IllegalBeanClassException, NoSuchConstructorException, NoUniqueConstructorException {
        if (singleton) {
            registerBeanProvider(name, new InjectingBeanProvider(new SingletonBeanProvider(new AnnotationConfigBeanProvider(beanClass))));
        } else {
            registerBeanProvider(name, new InjectingBeanProvider(new AnnotationConfigBeanProvider(beanClass)));
        }
    }

    @Override
    public void registerBeanProvider(String name, BeanProvider beanProvider) throws DuplicateBeanNameException {
        var oldValue = beanProviderMap.putIfAbsent(name, beanProvider);
        if (oldValue != null) {
            throw new DuplicateBeanNameException("重复的 bean name, name = [" + name + "]");
        }
    }

    @Override
    public void addBeanResolver(BeanResolver beanResolver) {
        this.beanResolvers.add(beanResolver);
    }

    @Override
    public List<BeanResolver> beanResolvers() {
        return beanResolvers;
    }

    @Override
    public void initializeBeans() {
        for (var entry : beanProviderMap.values()) {
            entry.getBean(this);
        }
    }

    @Override
    public String[] getBeanNames() {
        return beanProviderMap.keySet().toArray(String[]::new);
    }

    private BeanProvider findBeanProvider(String name) throws NoSuchBeanException {
        var beanProvider = beanProviderMap.get(name);
        if (beanProvider == null) {
            throw new NoSuchBeanException("未找到任何符合名称的 bean, name = [" + name + "]");
        }
        return beanProvider;
    }

    private BeanProvider findBeanProvider(Class<?> requiredType) throws NoSuchBeanException, NoUniqueBeanException {
        var beanProviders = beanProviderMap.values();
        var list = new ArrayList<BeanProvider>();
        for (var beanProvider : beanProviders) {
            if (requiredType.isAssignableFrom(beanProvider.beanClass())) {
                list.add(beanProvider);
            }
        }
        var size = list.size();
        if (size == 0) {
            throw new NoSuchBeanException("未找到任何符合类型的 bean, class = [" + requiredType.getName() + "]");
        }
        if (size > 1) {
            throw new NoUniqueBeanException("找到多个符合类型的 bean, class = [" + requiredType.getName() + "], 已找到 = [" + list.stream().map(c -> c.beanClass().getName()).collect(Collectors.joining(", ")) + "]");
        }
        return list.get(0);
    }

    private BeanProvider findBeanProvider(String name, Class<?> requiredType) throws NoSuchBeanException, NoUniqueBeanException {
        var beanProvider = findBeanProvider(name);
        if (requiredType.isAssignableFrom(beanProvider.beanClass())) {
            return beanProvider;
        }
        throw new NoSuchBeanException("未找到任何符合名称的 bean, name = [" + name + "], class = [" + requiredType.getName() + "]");
    }

}
