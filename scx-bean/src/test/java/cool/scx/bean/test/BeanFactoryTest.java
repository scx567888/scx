package cool.scx.bean.test;

import cool.scx.bean.BeanFactory;
import cool.scx.bean.BeanFactoryImpl;
import cool.scx.bean.annotation.Autowired;
import cool.scx.bean.annotation.Value;
import cool.scx.bean.resolver.AutowiredAnnotationResolver;
import cool.scx.bean.resolver.ValueAnnotationResolver;

import java.util.Map;

public class BeanFactoryTest {

    /// /////////////////////////////////////////
    // A - 正常单例注入
    public static class A {
        @Autowired
        public B b;
    }

    public static class B {
        @Autowired
        public C c;
    }

    public static class C {
    }

    /// /////////////////////////////////////////
    // D/E/F - 字段注入形成单例循环依赖（可解决）
    public static class D {
        @Autowired
        public E e;
    }

    public static class E {
        @Autowired
        public F f;
    }

    public static class F {
        @Autowired
        public D d;
    }

    /// /////////////////////////////////////////
    // G/H/I - 构造器循环依赖（不可解决）
    public static class G {
        public G(H h) {
        }
    }

    public static class H {
        public H(I i) {
        }
    }

    public static class I {
        public I(G g) {
        }
    }

    /// /////////////////////////////////////////
    // J/K/L - 多例正常依赖
    public static class J {
        @Autowired
        public K k;
    }

    public static class K {
        @Autowired
        public L l;
    }

    public static class L {
    }

    /// /////////////////////////////////////////
    // M/N/O - 多例循环依赖（应该抛异常）
    public static class M {
        @Autowired
        public N n;
    }

    public static class N {
        @Autowired
        public O o;
    }

    public static class O {
        @Autowired
        public M m;
    }

    /// /////////////////////////////////////////
    // P/Q - 单例依赖多例
    public static class P {
        @Autowired
        public Q q;
    }

    public static class Q {
    }

    /// /////////////////////////////////////////
    // R/S - 多例依赖单例
    public static class R {
        @Autowired
        public S s;
    }

    public static class S {
    }

    /// /////////////////////////////////////////
    // T/U/V - 字段+构造器混合注入
    public static class T {
        @Autowired
        public U u;

        public T(V v) {
        }
    }

    public static class U {
    }

    public static class V {
    }

    /// /////////////////////////////////////////
    // W/X/Y/Z - 不注入，普通 Bean
    public static class W {
    }

    public static class X {
    }

    public static class Y {

        @Value("key1")
        public String key1;

    }

    public static class Z {

        public Z(@Value("key2") int key2) {
            this.key2 = key2;
        }

        public int key2;

    }

    /// /////////////////////////////////////////
    // 主程序
    public static void main(String[] args) {
        var beanFactory = new BeanFactoryImpl();
        beanFactory.addBeanResolver(new AutowiredAnnotationResolver(beanFactory));
        beanFactory.addBeanResolver(new ValueAnnotationResolver(Map.of("key1", "Hello", "key2", "12345")));

        // 注册
        registerBeans(beanFactory);

        // 测试
        System.out.println("======== 测试 1：单例正常依赖 A -> B -> C ========");
        Object a = beanFactory.getBean(A.class);
        System.out.println(a);

        System.out.println("\n======== 测试 2：单例字段循环依赖 D -> E -> F -> D (字段注入可以解决) ========");
        Object d = beanFactory.getBean(D.class);
        System.out.println(d);

        System.out.println("\n======== 测试 3：单例构造器循环依赖 G -> H -> I -> G (应该抛异常) ========");
        try {
            G bean = beanFactory.getBean(G.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\n======== 测试 4：多例正常依赖 J -> K -> L ========");
        Object j1 = beanFactory.getBean(J.class);
        Object j2 = beanFactory.getBean(J.class);
        System.out.println(j1);
        System.out.println(j2);
        System.out.println("是否是不同实例？ " + (j1 != j2));

        System.out.println("\n======== 测试 5：多例循环依赖 M -> N -> O -> M (多例禁止循环，应该抛异常) ========");
        try {
            beanFactory.getBean(M.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\n======== 测试 6：单例依赖多例 P -> Q (允许) ========");
        Object p = beanFactory.getBean(P.class);
        System.out.println(p);

        System.out.println("\n======== 测试 7：多例依赖单例 R -> S (允许) ========");
        Object r1 = beanFactory.getBean(R.class);
        Object r2 = beanFactory.getBean(R.class);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println("是否是不同实例？ " + (r1 != r2));

        System.out.println("\n======== 测试 8：字段+构造器混合注入 T (字段U + 构造V) ========");
        Object t = beanFactory.getBean(T.class);
        System.out.println(t);

        System.out.println("\n======== 测试 9：普通无依赖 Bean W/X ========");
        System.out.println(beanFactory.getBean(W.class));
        System.out.println(beanFactory.getBean(X.class));

        System.out.println("\n======== 测试 10: @Value Bean Y/Z ========");
        Y y = beanFactory.getBean(Y.class);
        System.out.println("Y 的 key1 值 是否相等 :" + y.key1.equals("Hello"));
        Z z = beanFactory.getBean(Z.class);
        System.out.println("Z 的 key2 值 是否相等 :" + (z.key2 == 12345));
    }

    private static void registerBeans(BeanFactory beanFactory) {
        // 单例注册
        beanFactory.registerBeanClass("a", A.class);
        beanFactory.registerBeanClass("b", B.class);
        beanFactory.registerBeanClass("c", C.class);

        beanFactory.registerBeanClass("d", D.class);
        beanFactory.registerBeanClass("e", E.class);
        beanFactory.registerBeanClass("f", F.class);

        beanFactory.registerBeanClass("g", G.class);
        beanFactory.registerBeanClass("h", H.class);
        beanFactory.registerBeanClass("i", I.class);

        beanFactory.registerBeanClass("p", P.class);
        beanFactory.registerBeanClass("q", Q.class);

        beanFactory.registerBeanClass("s", S.class);

        beanFactory.registerBeanClass("t", T.class);
        beanFactory.registerBeanClass("u", U.class);
        beanFactory.registerBeanClass("v", V.class);

        beanFactory.registerBeanClass("we", W.class);
        beanFactory.registerBeanClass("x", X.class);
        beanFactory.registerBeanClass("y", Y.class);
        beanFactory.registerBeanClass("z", Z.class);

        // 多例注册
        beanFactory.registerBeanClass("j", J.class, false);
        beanFactory.registerBeanClass("k", K.class, false);
        beanFactory.registerBeanClass("l", L.class, false);

        beanFactory.registerBeanClass("m", M.class, false);
        beanFactory.registerBeanClass("n", N.class, false);
        beanFactory.registerBeanClass("o", O.class, false);

        beanFactory.registerBeanClass("r", R.class, false);
    }
}
