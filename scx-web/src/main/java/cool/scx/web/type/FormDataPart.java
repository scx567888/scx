package cool.scx.web.type;

import io.helidon.http.HttpMediaType;
import io.helidon.http.media.multipart.ReadablePart;

public class FormDataPart {

    private final String name;
    private final String fileName;
    private final HttpMediaType contentType;
    private final String fieldValue;
    private final byte[] uploadFileValue;

    public FormDataPart(ReadablePart part) {
        this.name = part.name();
        this.fileName = part.fileName().orElse(null);
        this.contentType = part.contentType();
        this.fieldValue = this.fileName == null ? part.as(String.class) : null;
        this.uploadFileValue = this.fileName == null ? null : part.as(byte[].class);
    }

    public boolean isFileUpload() {
        return this.fileName != null;
    }

    public String name() {
        return name;
    }

    public String fileName() {
        return fileName;
    }

    public HttpMediaType contentType() {
        return contentType;
    }

    public String fieldValue() {
        return fieldValue;
    }

    public byte[] uploadFileValue() {
        return uploadFileValue;
    }

}
