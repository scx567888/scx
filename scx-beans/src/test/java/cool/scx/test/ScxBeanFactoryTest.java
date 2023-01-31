package cool.scx.test;

import cool.scx.beans.ScxBeanFactory;
import org.testng.annotations.Test;

public class ScxBeanFactoryTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        var beanFactory = new ScxBeanFactory();
        beanFactory.register(A.class, B.class, C.class).refresh();
        var a = beanFactory.getBean(A.class);
        System.out.println(a.b.c.d);
    }

}
