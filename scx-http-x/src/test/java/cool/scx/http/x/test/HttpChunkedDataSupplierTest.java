package cool.scx.http.x.test;

import cool.scx.http.exception.BadRequestException;
import cool.scx.http.x.http1.chunked.HttpChunkedDataSupplier;
import cool.scx.io.data_node.DataNode;
import cool.scx.io.data_reader.DataReader;
import cool.scx.io.data_reader.LinkedDataReader;
import cool.scx.io.data_supplier.ByteArrayDataSupplier;
import cool.scx.io.exception.DataSupplierException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HttpChunkedDataSupplierTest {

    public static void main(String[] args) throws DataSupplierException {
        testGet();
        testFail();
        testFail2();
        testFail3();
    }

    @Test
    public static void testGet() throws DataSupplierException {
        // 模拟的复杂分块数据
        byte[] mockData = ("4\r\nWiki\r\n3\r\nHel\r\n7\r\nloWorld\r\n0\r\n\r\n").getBytes();
        DataReader byteArrayDataReader = new LinkedDataReader(new ByteArrayDataSupplier(mockData));

        // 创建 HttpChunkedDataSupplier 实例
        HttpChunkedDataSupplier supplier = new HttpChunkedDataSupplier(byteArrayDataReader);

        // 验证第一个数据块
        DataNode dataNode1 = supplier.get();
        Assert.assertEquals(dataNode1.toString(), "Wiki");

        // 验证第二个数据块
        DataNode dataNode2 = supplier.get();
        Assert.assertEquals(dataNode2.toString(), "Hel");

        // 验证第三个数据块
        DataNode dataNode3 = supplier.get();
        Assert.assertEquals(dataNode3.toString(), "loWorld");

        // 验证结束块
        DataNode dataNode4 = supplier.get();
        Assert.assertNull(dataNode4);

    }

    @Test
    public static void testFail() throws DataSupplierException {
        // 模拟的复杂分块数据
        byte[] mockData = ("4\r\nWiki\r\n3\r\nHel\r\n7\r\nloWorld\r\n0\r\n\r").getBytes();
        DataReader byteArrayDataReader = new LinkedDataReader(new ByteArrayDataSupplier(mockData));

        // 创建 HttpChunkedDataSupplier 实例
        HttpChunkedDataSupplier supplier = new HttpChunkedDataSupplier(byteArrayDataReader);

        // 验证第一个数据块
        DataNode dataNode1 = supplier.get();
        Assert.assertEquals(dataNode1.toString(), "Wiki");

        // 验证第二个数据块
        DataNode dataNode2 = supplier.get();
        Assert.assertEquals(dataNode2.toString(), "Hel");

        // 验证第三个数据块
        DataNode dataNode3 = supplier.get();
        Assert.assertEquals(dataNode3.toString(), "loWorld");

        // 验证结束块
        Assert.assertThrows(BadRequestException.class, supplier::get);

    }

    @Test
    public static void testFail2() throws DataSupplierException {
        // 模拟的复杂分块数据
        byte[] mockData = ("4\r\nWiki\r\n3\r\nHel\r\n7\r\nloWorld\r\n0\r\n???\r\n").getBytes();
        DataReader byteArrayDataReader = new LinkedDataReader(new ByteArrayDataSupplier(mockData));

        // 创建 HttpChunkedDataSupplier 实例
        HttpChunkedDataSupplier supplier = new HttpChunkedDataSupplier(byteArrayDataReader);

        // 验证第一个数据块
        DataNode dataNode1 = supplier.get();
        Assert.assertEquals(dataNode1.toString(), "Wiki");

        // 验证第二个数据块
        DataNode dataNode2 = supplier.get();
        Assert.assertEquals(dataNode2.toString(), "Hel");

        // 验证第三个数据块
        DataNode dataNode3 = supplier.get();
        Assert.assertEquals(dataNode3.toString(), "loWorld");

        // 验证结束块
        Assert.assertThrows(BadRequestException.class, supplier::get);

    }

    @Test
    public static void testFail3() throws DataSupplierException {
        // 模拟的复杂分块数据
        byte[] mockData = ("4\r\nWiki\r\n?\r\nHel\r\n7\r\nloWorld\r\n0\r\n\r\n").getBytes();
        DataReader byteArrayDataReader = new LinkedDataReader(new ByteArrayDataSupplier(mockData));

        // 创建 HttpChunkedDataSupplier 实例
        HttpChunkedDataSupplier supplier = new HttpChunkedDataSupplier(byteArrayDataReader);

        // 验证第一个数据块
        DataNode dataNode1 = supplier.get();
        Assert.assertEquals(dataNode1.toString(), "Wiki");

        // 验证第二个数据块
        Assert.assertThrows(BadRequestException.class, supplier::get);

    }

}
