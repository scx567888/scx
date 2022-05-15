package cool.scx;

public class Main {

    public static final ThreadLocal<Object> THREAD_LOCAL = new ThreadLocal<>();

    public static void main(String[] args) {
        f1();
        foo(i -> f1());
    }

    public static void f1() {
        Object o = THREAD_LOCAL.get();
        if (o == null) {
            System.out.println("not in foo");
        } else {
            System.out.println("in foo");
        }
    }

    public static void foo(Handler<Integer> handler) {
        new Thread(() -> {
            THREAD_LOCAL.set(new Object());
            handler.handle(10);
        }).start();
    }

    public interface Handler<A> {
        void handle(A a);
    }

}