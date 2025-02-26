package cool.scx.reflect.test;

import cool.scx.reflect.IClassInfo;
import cool.scx.reflect.ReflectHelper;
import org.testng.annotations.Test;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ClassInfoTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        var c1 = ReflectHelper.getClassInfo(c.class);
        var d2 = ReflectHelper.getClassInfo(d.class);

        var l = System.nanoTime();
        IClassInfo classInfo;
        for (int i = 0; i < 9999; i = i + 1) {

            classInfo = ReflectHelper.getClassInfo(a.class);
            classInfo = ReflectHelper.getClassInfo(c.class);
            classInfo = ReflectHelper.getClassInfo(d.class);
            classInfo = ReflectHelper.getClassInfo(ee.class);
        }

        System.out.println((System.nanoTime() - l) / 1000_000);
        var classInfo1 = ReflectHelper.getClassInfo(ee.class);
        var classInfo2 = ReflectHelper.getClassInfo(ee.b.getClass());

    }

    enum ee {
        a,
        b {

        }
    }

    @Repeatable(value = ab.class)
    @Retention(RetentionPolicy.RUNTIME)
    @interface aa {

    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface ab {
        aa[] value();
    }

    @aa
    @aa
    static class a {
        a gg;
    }

    @aa
    @aa
    static class c extends a {

    }

    @aa
    @aa
    static class d extends a {

    }

}
