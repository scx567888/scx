package cool.scx.http.media.multi_part;

import cool.scx.common.util.ArrayUtils;
import cool.scx.common.util.RandomUtils;
import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.io.InputStreamDataSupplier;
import cool.scx.io.LinkedDataReader;
import cool.scx.io.NoMatchFoundException;
import org.apache.commons.fileupload2.core.FileUploadSizeException;
import org.apache.commons.fileupload2.core.MultipartInput;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MultiPartStreamCached implements MultiPart, Iterator<MultiPartPart> {

    private final Path cachePath;
    private final LinkedDataReader linkedDataReader;
    private boolean hasNextPart;
    private String boundary;
    private final byte[] boundaryHeadCRLFBytes;
    private final byte[] boundaryENDBytes;
    private static final byte[] CRLF_CRLF_BYTES = "\r\n\r\n".getBytes();

    public MultiPartStreamCached(InputStream inputStream, String boundary, Path cachePath) {
        this.cachePath = cachePath;
        var boundaryHeadBytes = ArrayUtils.concat("--".getBytes(), boundary.getBytes());
        this.boundaryHeadCRLFBytes = ArrayUtils.concat(boundaryHeadBytes, "\r\n".getBytes());
        this.boundaryENDBytes = ArrayUtils.concat(boundaryHeadBytes, "--".getBytes());
        this.linkedDataReader = new LinkedDataReader(new InputStreamDataSupplier(inputStream));
        hasNextPart = readNext(linkedDataReader);
    }

    public ScxHttpHeadersWritable readToHeaders(LinkedDataReader multipartStream) throws MultipartInput.MalformedStreamException, FileUploadSizeException {
        // head 的终结点是 连续两个换行符 具体格式 如下
        // head /r/n
        // /r/n
        // content
        var headersBytes = multipartStream.readMatch(CRLF_CRLF_BYTES);
        var headersStr = new String(headersBytes);
        return ScxHttpHeaders.of(headersStr);
    }

    public byte[] readContentToByte(LinkedDataReader multipartStream) throws IOException {
        //我们需要查找终结点 先假设不是最后一个 那我们就需要查找下一个开始位置 
        try {
            var i = multipartStream.indexOf(boundaryHeadCRLFBytes);
            // i - 2 因为我们不需要读取内容结尾的 \r\n  
            var bytes = multipartStream.read(i - 2);
            //跳过 \r\n 方便后续读取
            multipartStream.skip(2);
            return bytes;
        } catch (NoMatchFoundException e) {
            //可能是最后一个查找 最终终结点
            var i = multipartStream.indexOf(boundaryENDBytes);
            var bytes = multipartStream.read(i - 2);
            //跳过 \r\n 方便后续读取
            multipartStream.skip(2);
            return bytes;
        }
    }

    public boolean readNext(LinkedDataReader multipartStream) {
        //查找 --xxxxxxxxx\r\n 没有代表 读取到结尾
        try {
            var i = multipartStream.indexOf(boundaryHeadCRLFBytes);
            multipartStream.skip(i + boundaryHeadCRLFBytes.length);
            return true;
        } catch (NoMatchFoundException e) {
            return false;
        }
    }

    public static boolean needCached(ScxHttpHeaders headers) {
        //根据是否存在文件名来假定上传的不是文件 只有文件才缓存
        var contentDisposition = headers.contentDisposition();
        if (contentDisposition == null) {
            return false;
        }
        var filename = contentDisposition.filename();
        return filename != null;
    }

    public Path readContentToPath(LinkedDataReader multipartStream, Path path) throws IOException {
        //保证一定有目录
        Files.createDirectories(path.getParent());
        var output = Files.newOutputStream(path);
        
        try (output){
            //我们需要查找终结点 先假设不是最后一个 那我们就需要查找下一个开始位置 
            try {
                var i = multipartStream.indexOf(boundaryHeadCRLFBytes);
                // i - 2 因为我们不需要读取内容结尾的 \r\n  
                multipartStream.read(output, i - 2);
                //跳过 \r\n 方便后续读取
                multipartStream.skip(2);
            } catch (NoMatchFoundException e) {
                //可能是最后一个查找 最终终结点
                var i = multipartStream.indexOf(boundaryENDBytes);
                multipartStream.read(output, i - 2);
                //跳过 \r\n 方便后续读取
                multipartStream.skip(2);
            }
        }
       
        return path;
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
            var headers = readToHeaders(linkedDataReader);

            var part = new MultiPartPartImpl().headers(headers);

            var b = needCached(headers);
            if (b) {
                var contentPath = readContentToPath(linkedDataReader, cachePath.resolve(RandomUtils.randomString(32)));
                part.body(contentPath);
            } else {
                var content = readContentToByte(linkedDataReader);
                part.body(content);
            }
            // 检查是否有下一个部分
            hasNextPart = readNext(linkedDataReader);

            return part;
        } catch (IOException e) {
            throw new RuntimeException("Error reading next part", e);
        }
    }

}
