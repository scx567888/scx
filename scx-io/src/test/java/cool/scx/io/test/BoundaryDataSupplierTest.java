package cool.scx.io.test;

import cool.scx.io.data_reader.LinkedDataReader;
import cool.scx.io.data_supplier.BoundaryDataSupplier;
import cool.scx.io.data_supplier.InputStreamDataSupplier;
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
        var rawDataReader = new LinkedDataReader(new InputStreamDataSupplier(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)), 1));
        var newDataReader = new LinkedDataReader(new BoundaryDataSupplier(rawDataReader, "hello".getBytes(StandardCharsets.UTF_8), 1));
        var read = newDataReader.read(Integer.MAX_VALUE);
        Assert.assertEquals(new String(read), "1234567890888866661111aaaabhellhellhellhhhhheeeeelllbbcccdddeeefffggghhhiii");
    }

}
