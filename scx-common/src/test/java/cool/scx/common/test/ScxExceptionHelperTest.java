package cool.scx.common.test;

import cool.scx.common.exception.ScxExceptionHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.CompletableFuture;

public class ScxExceptionHelperTest {

    public static void main(String[] args) {
        test1();
        test2();
        test3();
    }

    @Test
    public static void test1() {
        try {

            ScxExceptionHelper.wrap(() -> CompletableFuture.runAsync(() -> {
                var i = 1 / 0;
            }).get());
        } catch (Exception e) {
            var rootCause = ScxExceptionHelper.getRootCause(e);
            Assert.assertEquals(rootCause.getClass(), ArithmeticException.class);
        }
    }

    @Test
    public static void test2() {
        try {
            ScxExceptionHelper.wrap(() -> CompletableFuture.runAsync(() -> {
                var i = 1 / 0;
            }).join());
        } catch (Exception e) {
            var rootCause = ScxExceptionHelper.getRootCause(e);
            Assert.assertEquals(rootCause.getClass(), ArithmeticException.class);
        }

    }

    @Test
    public static void test3() {
        try {
            ScxExceptionHelper.wrap(() -> {
                var i = 1 / 0;
            });
        } catch (Exception e) {
            var rootCause = ScxExceptionHelper.getRootCause(e);
            Assert.assertEquals(rootCause.getClass(), ArithmeticException.class);
        }
    }

}
