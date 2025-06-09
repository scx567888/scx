package cool.scx.http.media.multi_part;

import cool.scx.io.data_indexer.KMPDataIndexer;
import cool.scx.io.data_node.DataNode;
import cool.scx.io.data_reader.DataReader;
import cool.scx.io.data_reader.LinkedDataReader;
import cool.scx.io.data_supplier.DataSupplier;
import cool.scx.io.data_supplier.InputStreamDataSupplier;
import cool.scx.io.exception.DataSupplierException;
import cool.scx.io.exception.NoMatchFoundException;
import cool.scx.io.exception.NoMoreDataException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class BoundaryDataSupplier implements DataSupplier {

    private final DataReader dataReader;
    private final KMPDataIndexer dataIndexer;
    private final int bufferLength;
    private boolean isFinish = false;

    public BoundaryDataSupplier(DataReader dataReader, byte[] boundaryBytes) {
        this.dataReader = dataReader;
        this.dataIndexer = new KMPDataIndexer(boundaryBytes);
        this.bufferLength = Math.max(boundaryBytes.length, 1);
    }

    public static void main(String[] args) {
        try (var in = Files.newInputStream(Path.of("C:\\Users\\scx\\Desktop\\AI 桥计划.txt"));) {
            var s = new LinkedDataReader(new InputStreamDataSupplier(in, 1));
            var my = new LinkedDataReader(new BoundaryDataSupplier(s, "AI学生打分".getBytes(StandardCharsets.UTF_8)));
            byte[] bytes = my.read(Integer.MAX_VALUE);
            var str = new String(bytes, StandardCharsets.UTF_8);
            System.out.println(str);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DataNode get() throws DataSupplierException {
        //完成了就永远返回 null
        if (isFinish) {
            return null;
        }
        try {
            // 在 bufferLength 范围内查找
            var index = dataReader.indexOf(dataIndexer, bufferLength, Long.MAX_VALUE);
            // 找到了就全部返回 
            var read = dataReader.read((int) index);
            // 找到了就没必要标识为完成
            isFinish = true;
            return new DataNode(read);
        } catch (NoMatchFoundException e) {
            // 没找到 说明可能还有 继续读取
            byte[] read = dataReader.read(bufferLength);
            return new DataNode(read);
        } catch (NoMoreDataException e) {
            // 如果底层 DataReader 没数据了，也返回 null
            return null;
        }
    }

}
