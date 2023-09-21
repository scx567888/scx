package cool.scx.enumeration;

/**
 * 文件格式 (文件类型/文件后缀/文件拓展名)
 *
 * @author scx567888
 * @version 1.3.4
 * @see <a href="https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types">https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types</a>
 */
public enum FileFormat {

    // Text
    TXT("text/plain"),
    HTML("text/html"),

    // Application
    BIN("application/octet-stream"),
    XML("application/xml"),
    JSON("application/json"),
    ZIP("application/zip"),
    PDF("application/pdf"),
    EPUB("application/epub+zip"),
    DOC("application/msword"),
    XLS("application/vnd.ms-excel"),
    PPT("application/vnd.ms-powerpoint"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation"),

    // Image
    BMP("image/bmp"),
    PNG("image/png"),
    JPEG("image/jpeg"),
    GIF("image/gif"),
    SVG("image/svg+xml"),
    WEBP("image/webp"),

    // Audio
    MP3("audio/mpeg"),
    WAV("audio/wav"),

    // Video
    MP4("video/mp4"),
    WEBM("video/webm");

    private final String mimeType;

    FileFormat(String memeType) {
        this.mimeType = memeType;
    }

    /**
     * 格式的 mimeType
     *
     * @return mimeType
     */
    public String mimeType() {
        return mimeType;
    }

}
