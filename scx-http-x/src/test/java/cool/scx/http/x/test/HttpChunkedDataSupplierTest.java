package cool.scx.http.x.test;

import cool.scx.io.ByteInput;
import cool.scx.io.DefaultByteInput;
import cool.scx.io.exception.ScxIOException;
import cool.scx.io.supplier.ByteArrayByteSupplier;
import cool.scx.http.exception.BadRequestException;
import cool.scx.http.x.http1.chunked.HttpChunkedByteSupplier;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HttpChunkedDataSupplierTest {

    public static void main(String[] args) throws ScxIOException {
        testGet();
        testFail();
        testFail2();
        testFail3();
    }

    @Test
    public static void testGet() throws ScxIOException {
        // 模拟的复杂分块数据
        byte[] mockData = ("4\r\nWiki\r\n3\r\nHel\r\n7\r\nloWorld\r\n0\r\n\r\n").getBytes();
        var byteArrayByteReader = new DefaultByteInput(new ByteArrayByteSupplier(mockData));

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
    public static void testFail() throws ScxIOException {
        // 模拟的复杂分块数据
        byte[] mockData = ("4\r\nWiki\r\n3\r\nHel\r\n7\r\nloWorld\r\n0\r\n\r").getBytes();
        DefaultByteInput byteArrayByteReader = new DefaultByteInput(new ByteArrayByteSupplier(mockData));

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
    public static void testFail2() throws ScxIOException {
        // 模拟的复杂分块数据
        byte[] mockData = ("4\r\nWiki\r\n3\r\nHel\r\n7\r\nloWorld\r\n0\r\n???\r\n").getBytes();
        DefaultByteInput byteArrayByteReader = new DefaultByteInput(new ByteArrayByteSupplier(mockData));

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
    public static void testFail3() throws ScxIOException {
        // 模拟的复杂分块数据
        byte[] mockData = ("4\r\nWiki\r\n?\r\nHel\r\n7\r\nloWorld\r\n0\r\n\r\n").getBytes();
        DefaultByteInput byteArrayByteReader = new DefaultByteInput(new ByteArrayByteSupplier(mockData));

        // 创建 HttpChunkedDataSupplier 实例
        HttpChunkedByteSupplier supplier = new HttpChunkedByteSupplier(byteArrayByteReader);

        // 验证第一个数据块
        var dataNode1 = supplier.get();
        Assert.assertEquals(dataNode1.toString(), "Wiki");

        // 验证第二个数据块
        Assert.assertThrows(BadRequestException.class, supplier::get);

    }

}
