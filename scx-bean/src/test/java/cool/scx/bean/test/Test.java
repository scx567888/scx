package cool.scx.bean.test;

import cool.scx.bean.x.AutowiredAnnotationBeanInjector;
import cool.scx.bean.x.BeanFactoryImpl;
import cool.scx.bean.x.ValueAnnotationBeanInjector;
import cool.scx.bean.x.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.Map;

public class Test {
    

    public static class A {
        
        @Autowired
        public B b;
        
        @Value("abc.password")
        public String bbb;

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
        beanFactory.addBeanInjector(new AutowiredAnnotationBeanInjector(beanFactory));
        beanFactory.addBeanInjector(new ValueAnnotationBeanInjector(Map.of("abc.password","你好")));
        
        beanFactory.registerBeanClass("a", A.class);
        beanFactory.registerBeanClass("b", B.class);
        beanFactory.registerBeanClass("c", C.class);
        beanFactory.registerBeanClass("d", D.class);
        beanFactory.registerBeanClass("f", F.class);
        beanFactory.registerBeanClass("g", G.class);
//        beanFactory.registerBeanClass("c",C.class);
//        Object a = beanFactory.getBean(G.class);
        Object a = beanFactory.getBean(A.class);
        System.out.println(a);
//        beanFactory.registerBeanCreator("b",new DynamicBeanCreator(()-> new B(),B.class));
//        beanFactory.initializeBeans();
//        Object a = beanFactory.getBean(C.class);
//        Object a1 = beanFactory.getBean("a");
//        Object b = beanFactory.getBean("b");
//        System.out.println(a);
    }

}

