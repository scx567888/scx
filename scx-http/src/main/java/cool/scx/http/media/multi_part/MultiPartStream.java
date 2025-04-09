package cool.scx.http.media.multi_part;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.io.data_reader.LinkedDataReader;
import cool.scx.io.exception.NoMatchFoundException;

import java.io.IOException;
import java.io.InputStream;
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
    protected final LinkedDataReader linkedDataReader;
    protected final byte[] boundaryBytes;
    protected boolean hasNextPart;
    protected String boundary;

    public MultiPartStream(InputStream inputStream, String boundary) {
        this.boundaryBytes = ("--" + boundary).getBytes();
        this.linkedDataReader = inputStreamToDataReader(inputStream);
        this.hasNextPart = readNext();
    }

    public ScxHttpHeadersWritable readToHeaders() {
        // head 的终结点是 连续两个换行符 具体格式 如下
        // head /r/n
        // /r/n
        // content
        var headersBytes = linkedDataReader.readUntil(CRLF_CRLF_BYTES);
        var headersStr = new String(headersBytes);
        return ScxHttpHeaders.of(headersStr,true);// 使用严格模式解析
    }

    public byte[] readContentToByte() throws IOException {
        //因为正常的表单一定是 --xxxxxx 结尾的 所以我们只需要找 下一个分块的起始位置作为结束位置即可 
        try {
            var i = linkedDataReader.indexOf(boundaryBytes);
            // i - 2 因为我们不需要读取内容结尾的 \r\n  
            var bytes = linkedDataReader.read((int) (i - 2));
            //跳过 \r\n 方便后续读取
            linkedDataReader.skip(2);
            return bytes;
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
        return hasNextPart;
    }

    @Override
    public MultiPartPart next() {
        if (!hasNextPart) {
            throw new NoSuchElementException("No more parts available.");
        }
        try {

            var part = new MultiPartPartImpl();

            // 读取当前部分的头部信息
            var headers = readToHeaders();
            part.headers(headers);

            //读取内容
            var content = readContentToByte();
            part.body(content);

            // 检查是否有下一个部分
            hasNextPart = readNext();

            return part;
        } catch (IOException e) {
            throw new RuntimeException("Error reading next part", e);
        }
    }

}
