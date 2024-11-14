package cool.scx.common.test;

import cool.scx.common.util.$;
import cool.scx.common.util.ScopedValue;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;


public class ScopedValueTest {

    public static void main(String[] args) throws Exception {
        test1();
    }

    @Test
    public static void test1() throws Exception {
        var l = new ArrayList<>();

        ScopedValue<Integer> i = ScopedValue.newInstance();

        ScopedValue.where(i, 100).run(() -> {
            $.sleep(100);
            var w = i.get();
            $.sleep(100);
            l.add(w);
            $.sleep(100);
        });

        var h = ScopedValue.where(i, 200).get(() -> {
            var w = i.get();
            $.sleep(100);
            return w;
        });

        l.add(h);

        ScopedValue.where(i, 300).call(() -> {
            var w = i.get();
            l.add(w);
            ScopedValue.where(i, 666).run(() -> {
                l.add(i.get());
            });
            return w;
        });

        Assert.assertEquals(l, List.of(100, 200, 300, 666));

    }

}
