package cool.scx.http.media.multi_part;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.ScxHttpHeadersWritable;
import org.apache.commons.fileupload2.core.FileUploadSizeException;
import org.apache.commons.fileupload2.core.MultipartInput;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MultiPartStream implements MultiPart, Iterator<MultiPartPart> {

    private final MultipartInput multipartStream;
    private boolean hasNextPart;
    private String boundary;

    public MultiPartStream(InputStream inputStream, String boundary) {
        var boundaryBytes = boundary.getBytes();
        try {
            this.multipartStream = MultipartInput.builder().setInputStream(inputStream).setBoundary(boundaryBytes).get();
            hasNextPart = multipartStream.skipPreamble();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ScxHttpHeadersWritable readToHeaders(MultipartInput multipartStream) throws MultipartInput.MalformedStreamException, FileUploadSizeException {
        var headersStr = multipartStream.readHeaders();
        return ScxHttpHeaders.of(headersStr);
    }

    public static byte[] readContentToByte(MultipartInput multipartStream) throws IOException {
        var output = new ByteArrayOutputStream();
        multipartStream.readBodyData(output);
        return output.toByteArray();
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

            // 读取当前部分的头部信息
            var headers = readToHeaders(multipartStream);

            var part = new MultiPartPartImpl().headers(headers);

            //读取内容
            var content = readContentToByte(multipartStream);
            part.body(content);

            // 检查是否有下一个部分
            hasNextPart = multipartStream.readBoundary();

            return part;
        } catch (IOException e) {
            throw new RuntimeException("Error reading next part", e);
        }
    }

}
