package cool.scx.data_reader.test;

import cool.scx.byte_reader.ByteReader;
import cool.scx.byte_reader.supplier.BoundaryByteSupplier;
import cool.scx.byte_reader.supplier.InputStreamByteSupplier;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class BoundaryDataSupplierTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        var str = "1234567890888866661111aaaabhellhellhellhhhhheeeeelllbbcccdddeeefffggghhhiiihelloend enden f";
        var rawDataReader = new ByteReader(new InputStreamByteSupplier(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)), 1));
        var newDataReader = new ByteReader(new BoundaryByteSupplier(rawDataReader, "hello".getBytes(StandardCharsets.UTF_8), 1));
        var read = newDataReader.read(Integer.MAX_VALUE);
        Assert.assertEquals(new String(read), "1234567890888866661111aaaabhellhellhellhhhhheeeeelllbbcccdddeeefffggghhhiii");
    }

}
