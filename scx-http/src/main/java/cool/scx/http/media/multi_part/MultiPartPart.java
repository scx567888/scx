package cool.scx.http.media.multi_part;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.content_disposition.ContentDisposition;
import cool.scx.http.content_disposition.ContentDispositionWritable;
import cool.scx.http.content_type.ContentType;
import cool.scx.http.content_type.ContentTypeWritable;

import static cool.scx.http.HttpFieldName.CONTENT_DISPOSITION;
import static cool.scx.http.HttpFieldName.CONTENT_TYPE;

//todo 这里目前是全部读取到内存中 这里可以优化 比如添加一个子类 将文件写入到 临时目录然后返回 文件路径 添加删除方法
public class MultiPartPart {

    private final ScxHttpHeadersWritable headers;
    private final ContentDispositionWritable contentDisposition;
    private final ContentTypeWritable contentType;
    private final byte[] content;

    public MultiPartPart(String headersStr, byte[] content) {
        this.headers = ScxHttpHeaders.of(headersStr);
        this.contentDisposition = ContentDisposition.of(headers.get(CONTENT_DISPOSITION));
        this.contentType = ContentType.of(headers.get(CONTENT_TYPE));
        this.content = content;
    }

    public ScxHttpHeadersWritable headers() {
        return headers;
    }

    public String name() {
        return contentDisposition != null ? contentDisposition.name() : null;
    }

    public String filename() {
        return contentDisposition != null ? contentDisposition.filename() : null;
    }

    public String size() {
        return contentDisposition != null ? contentDisposition.size() : null;
    }

    public ContentType contentType() {
        return contentType;
    }

    public byte[] content() {
        return content;
    }

}
