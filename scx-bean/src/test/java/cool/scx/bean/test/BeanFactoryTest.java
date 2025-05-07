package cool.scx.bean.test;

import cool.scx.bean.AutowiredAnnotationResolver;
import cool.scx.bean.BeanFactoryImpl;
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
        var beanFactory = new BeanFactoryImpl();
        //2, 创建处理器
        beanFactory.addBeanDependencyResolver(new AutowiredAnnotationResolver(beanFactory));
        //3, 注册 bean
        beanFactory.registerBeanClass(A.class.getName(), A.class);
        beanFactory.registerBeanClass(B.class.getName(), B.class);
        beanFactory.registerBeanClass(C.class.getName(), C.class);
        //3.1 初始化
        beanFactory.initializeBeans();
        //4, 获取 bean
        A bean = beanFactory.getBean(A.class);
        System.out.println(bean);
    }

}
