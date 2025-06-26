package cool.scx.http.x.test;

import cool.scx.bytes.ByteReader;
import cool.scx.bytes.exception.ByteSupplierException;
import cool.scx.bytes.supplier.ByteArrayByteSupplier;
import cool.scx.http.exception.BadRequestException;
import cool.scx.http.x.http1.chunked.HttpChunkedByteSupplier;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HttpChunkedDataSupplierTest {

    public static void main(String[] args) throws ByteSupplierException {
        testGet();
        testFail();
        testFail2();
        testFail3();
    }

    @Test
    public static void testGet() throws ByteSupplierException {
        // 模拟的复杂分块数据
        byte[] mockData = ("4\r\nWiki\r\n3\r\nHel\r\n7\r\nloWorld\r\n0\r\n\r\n").getBytes();
        ByteReader byteArrayByteReader = new ByteReader(new ByteArrayByteSupplier(mockData));

        // 创建 HttpChunkedDataSupplier 实例
        HttpChunkedByteSupplier supplier = new HttpChunkedByteSupplier(byteArrayByteReader);

        // 验证第一个数据块
        var dataNode1 = supplier.get();
        Assert.assertEquals(dataNode1.toString(), "Wiki");

        // 验证第二个数据块
        var dataNode2 = supplier.get();
        Assert.assertEquals(dataNode2.toString(), "Hel");

        // 验证第三个数据块
        var dataNode3 = supplier.get();
        Assert.assertEquals(dataNode3.toString(), "loWorld");

        // 验证结束块
        var dataNode4 = supplier.get();
        Assert.assertNull(dataNode4);

    }

    @Test
    public static void testFail() throws ByteSupplierException {
        // 模拟的复杂分块数据
        byte[] mockData = ("4\r\nWiki\r\n3\r\nHel\r\n7\r\nloWorld\r\n0\r\n\r").getBytes();
        ByteReader byteArrayByteReader = new ByteReader(new ByteArrayByteSupplier(mockData));

        // 创建 HttpChunkedDataSupplier 实例
        HttpChunkedByteSupplier supplier = new HttpChunkedByteSupplier(byteArrayByteReader);

        // 验证第一个数据块
        var dataNode1 = supplier.get();
        Assert.assertEquals(dataNode1.toString(), "Wiki");

        // 验证第二个数据块
        var dataNode2 = supplier.get();
        Assert.assertEquals(dataNode2.toString(), "Hel");

        // 验证第三个数据块
        var dataNode3 = supplier.get();
        Assert.assertEquals(dataNode3.toString(), "loWorld");

        // 验证结束块
        Assert.assertThrows(BadRequestException.class, supplier::get);

    }

    @Test
    public static void testFail2() throws ByteSupplierException {
        // 模拟的复杂分块数据
        byte[] mockData = ("4\r\nWiki\r\n3\r\nHel\r\n7\r\nloWorld\r\n0\r\n???\r\n").getBytes();
        ByteReader byteArrayByteReader = new ByteReader(new ByteArrayByteSupplier(mockData));

        // 创建 HttpChunkedDataSupplier 实例
        HttpChunkedByteSupplier supplier = new HttpChunkedByteSupplier(byteArrayByteReader);

        // 验证第一个数据块
        var dataNode1 = supplier.get();
        Assert.assertEquals(dataNode1.toString(), "Wiki");

        // 验证第二个数据块
        var dataNode2 = supplier.get();
        Assert.assertEquals(dataNode2.toString(), "Hel");

        // 验证第三个数据块
        var dataNode3 = supplier.get();
        Assert.assertEquals(dataNode3.toString(), "loWorld");

        // 验证结束块
        Assert.assertThrows(BadRequestException.class, supplier::get);

    }

    @Test
    public static void testFail3() throws ByteSupplierException {
        // 模拟的复杂分块数据
        byte[] mockData = ("4\r\nWiki\r\n?\r\nHel\r\n7\r\nloWorld\r\n0\r\n\r\n").getBytes();
        ByteReader byteArrayByteReader = new ByteReader(new ByteArrayByteSupplier(mockData));

        // 创建 HttpChunkedDataSupplier 实例
        HttpChunkedByteSupplier supplier = new HttpChunkedByteSupplier(byteArrayByteReader);

        // 验证第一个数据块
        var dataNode1 = supplier.get();
        Assert.assertEquals(dataNode1.toString(), "Wiki");

        // 验证第二个数据块
        Assert.assertThrows(BadRequestException.class, supplier::get);

    }

}
