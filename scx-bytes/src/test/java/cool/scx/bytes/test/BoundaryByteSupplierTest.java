package cool.scx.bytes.test;

import cool.scx.bytes.ByteReader;
import cool.scx.bytes.supplier.BoundaryByteSupplier;
import cool.scx.bytes.supplier.InputStreamByteSupplier;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class BoundaryByteSupplierTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        var str = "1234567890888866661111aaaabhellhellhellhhhhheeeeelllbbcccdddeeefffggghhhiiihelloend enden f";
        var rawByteReader = new ByteReader(new InputStreamByteSupplier(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)), 1));
        var newByteReader = new ByteReader(new BoundaryByteSupplier(rawByteReader, "hello".getBytes(StandardCharsets.UTF_8), 1));
        var read = newByteReader.read(Integer.MAX_VALUE);
        Assert.assertEquals(new String(read), "1234567890888866661111aaaabhellhellhellhhhhheeeeelllbbcccdddeeefffggghhhiii");
    }

}
