package cool.scx.io.test;

import cool.scx.bytes.supplier.ByteArrayByteSupplier;
import cool.scx.io.io_stream.ByteReaderInputStream;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class MarkTest {

    public static void main(String[] args) throws IOException {
        test1();
    }

    @Test
    public static void test1() throws IOException {
        var data = "你好".repeat(100) + "终结符";
        var s = new ByteReaderInputStream(new ByteArrayByteSupplier(data.getBytes()));
        s.mark(0);
        var b1 = s.readNBytes(200);
        s.reset();
        byte[] b2 = s.readNBytes(200);
        Assert.assertEquals(b1, b2);
    }

}
