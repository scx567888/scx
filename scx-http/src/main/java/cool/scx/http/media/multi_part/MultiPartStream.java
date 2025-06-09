package cool.scx.http.media.multi_part;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.io.data_reader.DataReader;
import cool.scx.io.data_reader.LinkedDataReader;
import cool.scx.io.data_supplier.BoundaryDataSupplier;
import cool.scx.io.exception.NoMatchFoundException;
import cool.scx.io.io_stream.DataReaderInputStream;
import cool.scx.io.io_stream.StreamClosedException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static cool.scx.io.IOHelper.inputStreamToDataReader;

/// MultiPartStream
/// todo 这里没有关闭流 可能出现问题
///
/// @author scx567888
/// @version 0.0.1
public class MultiPartStream implements MultiPart, Iterator<MultiPartPart> {

    protected static final byte[] CRLF_CRLF_BYTES = "\r\n\r\n".getBytes();
    protected final DataReader linkedDataReader;
    protected final byte[] boundaryBytes;
    protected boolean hasNextPart;
    protected String boundary;
    protected MultiPartPart lastPart;

    public MultiPartStream(InputStream inputStream, String boundary) {
        this.boundary = boundary;
        this.boundaryBytes = ("--" + boundary).getBytes();
        this.linkedDataReader = inputStreamToDataReader(inputStream);
//        this.hasNextPart = readNext();
    }

    public ScxHttpHeadersWritable readToHeaders() {
        // head 的终结点是 连续两个换行符 具体格式 如下
        // head /r/n
        // /r/n
        // content
        var headersBytes = linkedDataReader.readUntil(CRLF_CRLF_BYTES);
        var headersStr = new String(headersBytes);
        return ScxHttpHeaders.ofStrict(headersStr);// 使用严格模式解析
    }

    public InputStream readContentToByte() throws IOException {
        //因为正常的表单一定是 --xxxxxx 结尾的 所以我们只需要找 下一个分块的起始位置作为结束位置即可 
        try {
            var s=("\r\n--"+boundary).getBytes();
            //todo 这里不对
            var c=new LinkedDataReader(new BoundaryDataSupplier(linkedDataReader, s));
//            var i = linkedDataReader.indexOf(boundaryBytes);
            // i - 2 因为我们不需要读取内容结尾的 \r\n  
//            var bytes = linkedDataReader.read((int) (i - 2));
            //跳过 \r\n 方便后续读取
//            linkedDataReader.skip(2);
            return new DataReaderInputStream(c);
        } catch (NoMatchFoundException e) {
            // 理论上一个正常的 MultiPart 不会有这种情况
            throw new RuntimeException("异常状态 !!!");
        }
    }

    public boolean readNext() {
        //查找 --xxxxxxxxx
        try {
            var i = linkedDataReader.indexOf(boundaryBytes);
            linkedDataReader.skip(i + boundaryBytes.length);
            //向后读取两个字节 
            var a = linkedDataReader.read();
            var b = linkedDataReader.read();
            // 判断 是 \r\n or -- 
            if (a == '\r' && b == '\n') { //还有数据
                return true;
            } else if (a == '-' && b == '-') { // 读取到了终结符
                return false;
            } else { // 理论上一个正常的 MultiPart 不会有这种情况
                throw new RuntimeException("未知字符 !!! ");
            }
        } catch (NoMatchFoundException e) {
            return false;
        }
    }

    @Override
    public String boundary() {
        return boundary;
    }

    @Override
    public Iterator<MultiPartPart> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        if(lastPart != null) {
            // 只有完全消费了上一个流我们才知道有没有下一个流
            consumeInputStream(lastPart.inputStream());    
        }
        // 向后查看
        byte[] peek = linkedDataReader.peek(boundaryBytes.length + 2);
        var s=new String(peek);
        System.out.println();
        // 1. 先判断 peek 开头是否和 boundaryBytes 匹配
        for (int i = 0; i < boundaryBytes.length; i++) {
            if (peek[i] != boundaryBytes[i]) {
                throw new RuntimeException("Malformed multipart: boundary not matched");
            }
        }

        // 2. boundary 后两个字节判断
        byte a = peek[boundaryBytes.length];
        byte b = peek[boundaryBytes.length + 1];

        if (a == '-' && b == '-') {
            // 遇到 --boundary-- ，整个 multipart 结束
            return false;
        } else if (a == '\r' && b == '\n') {
            // 遇到 --boundary\r\n ，还有下一个 part
            return true;
        } else {
            throw new RuntimeException("Malformed multipart: invalid boundary ending");
        }
        //1, 判断是否相同
        //2, 检查末尾字符 \r\n 表示接下来有内容 -- 表示结束 其他表示 异常分块
    }

    @Override
    public MultiPartPart next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more parts available.");
        }
        try {
            if (lastPart != null) {
                consumeInputStream(lastPart.inputStream());
                //移除
                linkedDataReader.skip(2);
            }

            linkedDataReader.skip(boundaryBytes.length+2);

            var part = new MultiPartPartImpl();

            // 读取当前部分的头部信息
            var headers = readToHeaders();
            part.headers(headers);

            //读取内容
            var content = readContentToByte();
            part.body(content);
            
            lastPart = part;

            // 检查是否有下一个部分
//            hasNextPart = readNext();

            return part;
        } catch (IOException e) {
            throw new RuntimeException("Error reading next part", e);
        }
    }

    public static void consumeInputStream(InputStream inputStream) {
        try (inputStream) {
            inputStream.transferTo(OutputStream.nullOutputStream());
        } catch (StreamClosedException | IOException e) {
            // 忽略
        }
    }

}
