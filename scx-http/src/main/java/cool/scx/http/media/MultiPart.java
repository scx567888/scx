package cool.scx.http.media;

import org.apache.commons.fileupload.MultipartStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MultiPart implements Iterable<MultiPartPart>, Iterator<MultiPartPart> {

    private final MultipartStream multipartStream;
    private boolean hasNextPart;

    public MultiPart(InputStream inputStream, String boundary) {
        var boundaryBytes = boundary.getBytes();
        this.multipartStream = new MultipartStream(inputStream, boundaryBytes, 1024, null);
        try {
            hasNextPart = multipartStream.skipPreamble();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            var headers = multipartStream.readHeaders();

            //读取内容
            var output = new ByteArrayOutputStream();
            multipartStream.readBodyData(output);
            var content = output.toByteArray();

            // 检查是否有下一个部分
            hasNextPart = multipartStream.readBoundary();

            return new MultiPartPart(headers, content);
        } catch (IOException e) {
            throw new RuntimeException("Error reading next part", e);
        }
    }

    @Override
    public Iterator<MultiPartPart> iterator() {
        return this;
    }

}
