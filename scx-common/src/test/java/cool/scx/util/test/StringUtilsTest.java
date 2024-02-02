package cool.scx.util.test;

import cool.scx.util.StringUtils;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class StringUtilsTest {

    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
    }

    @Test
    public static void test1() {
        var correctResults = "?, ?, ?, ?";
        var finalResult = StringUtils.repeat("?", ", ", 4);
        assertEquals(correctResults, finalResult);
    }

    @Test
    public static void test2() {
        var correctResults = true;
        var finalResult = StringUtils.isBlank(" ");
        assertEquals(correctResults, finalResult);
    }

    @Test
    public static void test3() {
        var correctResults = true;
        var finalResult = StringUtils.isEmpty("");
        assertEquals(correctResults, finalResult);
    }

    @Test
    public static void test4() {
        var correctResults = new String[]{"🐷", "😂", "🤣", "😅", "😍", "😡", "1", "2", "3", "你", "好"};
        var finalResult = StringUtils.split("🐷😂🤣😅😍😡123你好");
        assertEquals(correctResults, finalResult);
    }

}
