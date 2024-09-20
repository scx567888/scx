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
        try {
            this.uploadFileValue = null;
            if (this.fileName == null) {
            } else {
//                var path = part.as(String.class);
                System.out.println();
                try (var s = part.inputStream()) {
                    var ss = s.readAllBytes();
                    System.out.println();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
