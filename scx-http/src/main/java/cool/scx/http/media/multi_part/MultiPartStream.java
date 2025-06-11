package cool.scx.http.media.multi_part;

import cool.scx.byte_reader.ByteReader;
import cool.scx.byte_reader.supplier.BoundaryByteSupplier;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.io.io_stream.ByteReaderInputStream;
import cool.scx.io.io_stream.StreamClosedException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static cool.scx.io.IOHelper.inputStreamToByteReader;

/// MultiPartStream
///
/// @author scx567888
/// @version 0.0.1
public class MultiPartStream implements MultiPart, Iterator<MultiPartPart>, AutoCloseable {

    private static final byte[] CRLF_CRLF_BYTES = "\r\n\r\n".getBytes();
    private final String boundary; // xxx
    private final byte[] boundaryBytes; // --xxx
    private final byte[] boundaryStartBytes; // \r\b--xxx
    private final ByteReader linkedByteReader;
    private MultiPartPart lastPart;

    public MultiPartStream(InputStream inputStream, String boundary) {
        this.boundary = boundary;
        this.boundaryBytes = ("--" + boundary).getBytes();
        this.boundaryStartBytes = ("\r\n--" + boundary).getBytes();
        this.linkedByteReader = inputStreamToByteReader(inputStream);
        this.lastPart = null;
    }

    public static void consumeInputStream(InputStream inputStream) {
        try (inputStream) {
            inputStream.transferTo(OutputStream.nullOutputStream());
        } catch (StreamClosedException | IOException e) {
            // 忽略
        }
    }

    public ScxHttpHeadersWritable readHeaders() {
        // head 的终结点是 连续两个换行符 具体格式 如下
        // head /r/n
        // /r/n
        // content
        var headersBytes = linkedByteReader.readUntil(CRLF_CRLF_BYTES);
        var headersStr = new String(headersBytes);
        return ScxHttpHeaders.ofStrict(headersStr);// 使用严格模式解析
    }

    public InputStream readContent() {
        // 内容 的终结符是 \r\n--boundary
        // 所以我们创建一个以 \r\n--boundary 结尾的分割符 输入流
        return new ByteReaderInputStream(new ByteReader(new BoundaryByteSupplier(linkedByteReader, boundaryStartBytes)));
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
        // 用户可能并没有消耗掉上一个分块就调用了 hasNext 这里我们替他消费 
        if (lastPart != null) {
            //消费掉上一个分块的内容
            consumeInputStream(lastPart.inputStream());
            // inputStream 中并不会消耗 最后的 \r\n, 但是接下来的判断我们也不需要 所以这里 跳过最后的 \r\n
            linkedByteReader.skip(2);
            //置空 防止重复消费
            lastPart = null;
        }

        // 下面的操作不会移动指针 所以我们可以 重复调用 hasNext 
        // 向后查看
        var peek = linkedByteReader.peek(boundaryBytes.length + 2);

        // 这种情况只可能发生在流已经提前结束了
        if (peek.length != boundaryBytes.length + 2) {
            throw new RuntimeException("Malformed multipart: boundary peek too short");
        }

        // 1. 先判断 peek 开头是否和 boundaryBytes 匹配
        for (int i = 0; i < boundaryBytes.length; i++) {
            if (peek[i] != boundaryBytes[i]) {
                throw new RuntimeException("Malformed multipart: boundary not matched");
            }
        }

        // 2. boundary 后两个字节判断
        byte a = peek[peek.length - 2];
        byte b = peek[peek.length - 1];

        if (a == '-' && b == '-') {
            // 遇到 --boundary-- ，整个 multipart 结束
            return false;
        } else if (a == '\r' && b == '\n') {
            // 遇到 --boundary\r\n ，还有下一个 part
            return true;
        } else {
            //其他字符那就只能抛异常了
            throw new RuntimeException("Malformed multipart: invalid boundary ending");
        }

    }

    @Override
    public MultiPartPart next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more parts available.");
        }

        // 跳过起始的 --boundary\r\n
        linkedByteReader.skip(boundaryBytes.length + 2);

        var part = new MultiPartPartImpl();

        // 读取当前部分的头部信息
        var headers = readHeaders();
        part.headers(headers);

        // 读取内容
        var content = readContent();
        part.body(content);

        lastPart = part;

        return part;

    }

    @Override
    public void close() throws Exception {
        // todo 这里没有关闭流 可能出现问题
    }

}
