package cool.scx.test;

import cool.scx.functional.ScxHandler;
import org.testng.annotations.Test;

public class ScxHandlerTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        ScxHandler handler = () -> System.out.println(123);
        handler.handle();
    }

}
