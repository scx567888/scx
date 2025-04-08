package cool.scx.bean.test;

import cool.scx.bean.AnnotatedGenericBeanDefinition;
import cool.scx.bean.AutowiredAnnotationBeanPostProcessor;
import cool.scx.bean.DefaultListableBeanFactory;
import cool.scx.bean.test.b.A;
import cool.scx.bean.test.b.B;
import cool.scx.bean.test.b.C;
import org.testng.annotations.Test;

public class BeanFactoryTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        //1, 创建 beanFactory
        var beanFactory = new DefaultListableBeanFactory();
        //2, 创建处理器
        var beanPostProcessor = new AutowiredAnnotationBeanPostProcessor();
        beanPostProcessor.setBeanFactory(beanFactory);
        beanFactory.addBeanPostProcessor(beanPostProcessor);
        //3, 注册 bean
        beanFactory.registerBeanDefinition(A.class.getName(), new AnnotatedGenericBeanDefinition(A.class));
        beanFactory.registerBeanDefinition(B.class.getName(), new AnnotatedGenericBeanDefinition(B.class));
        beanFactory.registerBeanDefinition(C.class.getName(), new AnnotatedGenericBeanDefinition(C.class));
        //3.1 初始化
        beanFactory.preInstantiateSingletons();
        //4, 获取 bean
        A bean = beanFactory.getBean(A.class);
        System.out.println(bean);
    }

}
