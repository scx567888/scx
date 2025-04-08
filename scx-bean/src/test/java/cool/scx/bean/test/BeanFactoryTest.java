package cool.scx.bean.test;

import cool.scx.bean.AnnotatedGenericBeanDefinition;
import cool.scx.bean.DefaultListableBeanFactory;
import cool.scx.bean.test.b.A;
import cool.scx.bean.test.b.B;
import cool.scx.bean.test.b.C;

public class BeanFactoryTest {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        var beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition(A.class.getName(), new AnnotatedGenericBeanDefinition(A.class));
        beanFactory.registerBeanDefinition(B.class.getName(), new AnnotatedGenericBeanDefinition(B.class));
        beanFactory.registerBeanDefinition(C.class.getName(), new AnnotatedGenericBeanDefinition(C.class));
        A bean = beanFactory.getBean(A.class);
        System.out.println(bean);
    }

}
