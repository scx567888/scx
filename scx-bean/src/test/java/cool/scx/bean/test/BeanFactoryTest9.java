package cool.scx.bean.test;

import cool.scx.bean.BeanFactoryImpl;
import cool.scx.bean.annotation.Autowired;
import cool.scx.bean.resolver.AutowiredAnnotationResolver;

public class BeanFactoryTest9 {
    
    public static void main(String[] args) {
        var beanFactory = new BeanFactoryImpl();
        beanFactory.addBeanResolver(new AutowiredAnnotationResolver(beanFactory));
        beanFactory.registerBeanClass("a",A.class);
        beanFactory.registerBeanClass("b",B.class);
        beanFactory.registerBeanClass("c",C.class);
        beanFactory.registerBeanClass("w",W.class);

        W bean = beanFactory.getBean(W.class);
        System.out.println(bean);
        
    }
    
    public static class A {

        @Autowired
        public B a;

        @Autowired
        public C c;
        
    }

    public static class B {

        @Autowired
        public C b;
        
    }

    public static class C {

        @Autowired
        public A a;
        
    }


    public static class W {

        public W(A a, B b, C c) {
            
        }
    }
    
}
