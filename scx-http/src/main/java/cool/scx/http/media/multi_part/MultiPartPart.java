package cool.scx.http.media.multi_part;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.content_disposition.ContentDisposition;
import cool.scx.http.content_disposition.ContentDispositionWritable;
import cool.scx.http.content_type.ContentType;
import cool.scx.http.content_type.ContentTypeWritable;

import static cool.scx.http.HttpFieldName.CONTENT_DISPOSITION;
import static cool.scx.http.HttpFieldName.CONTENT_TYPE;


public class MultiPartPart {

    protected final ScxHttpHeaders headers;
    protected final ContentDisposition contentDisposition;
    protected final ContentType contentType;
    protected final String name;
    protected final String filename;
    protected final String size;
    protected final byte[] content;
    protected final boolean isFile;

    public MultiPartPart(ScxHttpHeaders headers, byte[] content) {
        this.headers = headers;
        this.contentDisposition = headers.contentDisposition();
        this.contentType = headers.contentType();
        this.content = content;
        if (this.contentDisposition != null) {
            this.name = contentDisposition.name();
            this.filename = contentDisposition.filename();
            this.size = contentDisposition.size();
            this.isFile = filename != null;
        } else {
            this.name = null;
            this.filename = null;
            this.size = null;
            this.isFile = false;
        }
    }

    public ScxHttpHeaders headers() {
        return headers;
    }

    public boolean isFile() {
        return isFile;
    }

    public String name() {
        return name;
    }

    public String filename() {
        return filename;
    }

    public String size() {
        return size;
    }

    public ContentType contentType() {
        return contentType;
    }

    public ContentDisposition contentDisposition() {
        return contentDisposition;
    }

    public byte[] content() {
        return content;
    }

}
