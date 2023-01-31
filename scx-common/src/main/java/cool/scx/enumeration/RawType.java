package cool.scx.enumeration;

import io.vertx.core.http.impl.MimeMapping;

/**
 * <p>RawType class.</p>
 *
 * @author scx567888
 * @version 1.3.4
 */
public enum RawType {

    /**
     * 未知的 二进制类型
     */
    BIN,

    /**
     * 文本类型
     */
    TXT,

    XML,

    DOCX,

    DOC,

    XLS,

    XLSX,

    PPT,

    PPTX,

    ZIP,

    PNG,

    JPG,

    GIF,

    MP3,

    MP4;

    /**
     * <p>Constructor for RawType.</p>
     */
    private final String mimeType;

    RawType() {
        this.mimeType = MimeMapping.getMimeTypeForExtension(this.name().toLowerCase());
    }

    /**
     * <p>mimeType.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String mimeType() {
        return mimeType;
    }

}
