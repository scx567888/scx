package cool.scx.bean.test;

import cool.scx.bean.BeanFactoryImpl;
import cool.scx.bean.annotation.PreferredConstructor;
import cool.scx.bean.exception.NoSuchBeanException;
import cool.scx.bean.exception.NoSuchConstructorException;
import cool.scx.bean.exception.NoUniqueConstructorException;
import cool.scx.bean.resolver.AutowiredAnnotationResolver;
import cool.scx.bean.resolver.ValueAnnotationResolver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class BeanFactoryTest4 {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        var beanFactory = new BeanFactoryImpl();
        beanFactory.addBeanResolver(new AutowiredAnnotationResolver(beanFactory));
        beanFactory.addBeanResolver(new ValueAnnotationResolver(Map.of("key1", "Hello", "key2", "12345")));

        beanFactory.registerBeanClass("a", A.class);
        beanFactory.registerBeanClass("b", B.class);
        // 注册阶段就会报错
        Assert.expectThrows(NoUniqueConstructorException.class, () -> {
            beanFactory.registerBeanClass("c", C.class);
        });
        beanFactory.registerBeanClass("d", D.class);
        Assert.expectThrows(NoSuchConstructorException.class, () -> {
            beanFactory.registerBeanClass("e", E.class);
        });
        Assert.expectThrows(NoUniqueConstructorException.class, () -> {
            beanFactory.registerBeanClass("f", F.class);
        });

        //正常获取
        A a = beanFactory.getBean(A.class);
        B b = beanFactory.getBean(B.class);

        Assert.expectThrows(NoSuchBeanException.class, () -> {
            C c = beanFactory.getBean(C.class);
        });

        beanFactory.getBean(D.class);
        Assert.expectThrows(NoSuchBeanException.class, () -> {
            beanFactory.getBean(E.class);
        });
        Assert.expectThrows(NoSuchBeanException.class, () -> {
            beanFactory.getBean(F.class);
        });
    }

    public static class A {

    }

    public static class B {
        public B() {
        }
    }

    public static class C {
        public C() {
        }

        public C(int a) {
        }
    }

    public static class D {

        @PreferredConstructor
        public D() {

        }

        public D(int a) {

        }

    }

    public static class E {
        private E() {

        }
    }

    public static class F {
        @PreferredConstructor
        public F() {

        }

        @PreferredConstructor
        public F(int a) {

        }
    }

}
