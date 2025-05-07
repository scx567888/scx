package cool.scx.bean.test;

import cool.scx.bean.AutowiredAnnotationResolver;
import cool.scx.bean.BeanFactoryImpl;
import cool.scx.bean.ValueAnnotationResolver;
import cool.scx.bean.annotation.Autowired;
import cool.scx.bean.annotation.Value;

import javax.sql.DataSource;
import java.util.Map;

public class Test {
    

    public static class A {
        
        private final int w;
        
        @Autowired
        public A a;
        
        @Autowired
        public B b;
        
        @Value("key1")
        public String bbb;

        public A(@Value("key2") int w) {
            this.w = w;
        }
    }

    public static class B {
        
        @Autowired
        public A a;
        
    }

    public static class C {

        @Autowired
        public A a;

    }
    
    public static class D {
        public D(F f) {
        }
    }

    public static class F {
        public F(D d) {
        }
    }

    public static class G {
        public final DataSource dataSource;

        public G(DataSource dataSource) {
            this.dataSource=dataSource;
        }
    }

    public static void main(String[] args) {
        var beanFactory = new BeanFactoryImpl();
        beanFactory.addBeanDependencyResolver(new AutowiredAnnotationResolver(beanFactory));
        beanFactory.addBeanDependencyResolver(new ValueAnnotationResolver(Map.of("key1","你好","key2","888")));
        
        beanFactory.registerBeanClass("a", A.class);
        beanFactory.registerBeanClass("b", B.class);
        beanFactory.registerBeanClass("c", C.class);
        beanFactory.registerBeanClass("d", D.class);
        beanFactory.registerBeanClass("f", F.class);
        beanFactory.registerBeanClass("g", G.class);

        Object a = beanFactory.getBean(A.class);
        System.out.println(a);
        System.out.println(beanFactory.getBean(F.class));
    }

}

