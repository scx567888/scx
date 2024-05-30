package cool.scx.common.standard;

import java.nio.charset.Charset;

/**
 * MediaType
 *
 * @author scx567888
 * @version 0.0.1
 * @see <a href="https://www.iana.org/assignments/media-types/media-types.xhtml">https://www.iana.org/assignments/media-types/media-types.xhtml</a>
 */
public enum MediaType {

    // Text
    TEXT_PLAIN("text", "plain"),
    TEXT_HTML("text", "html"),

    // Application
    APPLICATION_OCTET_STREAM("application", "octet-stream"),
    APPLICATION_X_WWW_FORM_URLENCODED("application", "x-www-form-urlencoded"),
    APPLICATION_XML("application", "xml"),
    APPLICATION_JSON("application", "json"),
    APPLICATION_ZIP("application", "zip"),
    APPLICATION_PDF("application", "pdf"),
    APPLICATION_DOC("application", "msword"),
    APPLICATION_XLS("application", "vnd.ms-excel"),
    APPLICATION_PPT("application", "vnd.ms-powerpoint"),
    APPLICATION_DOCX("application", "vnd.openxmlformats-officedocument.wordprocessingml.document"),
    APPLICATION_XLSX("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    APPLICATION_PPTX("application", "vnd.openxmlformats-officedocument.presentationml.presentation"),

    // Image
    IMAGE_BMP("image", "bmp"),
    IMAGE_PNG("image", "png"),
    IMAGE_JPEG("image", "jpeg"),
    IMAGE_GIF("image", "gif"),
    IMAGE_SVG("image", "svg+xml"),
    IMAGE_WEBP("image", "webp"),

    // Audio
    AUDIO_MPEG("audio", "mpeg"),
    AUDIO_WAV("audio", "wav"),

    // Video
    VIDEO_MP4("video", "mp4"),
    VIDEO_WEBM("video", "webm"),

    // Multipart
    MULTIPART_FORM_DATA("multipart", "form-data");

    private final String type;
    private final String subtype;
    private final String value;

    MediaType(String type, String subtype) {
        this.type = type;
        this.subtype = subtype;
        this.value = type + "/" + subtype;
    }

    public static MediaType ofExtension(String ext) {
        var fileFormat = FileFormat.ofExtension(ext);
        return fileFormat != null ? fileFormat.mediaType() : null;
    }

    public static MediaType ofFileName(String filename) {
        var fileFormat = FileFormat.ofFileName(filename);
        return fileFormat != null ? fileFormat.mediaType() : null;
    }

    public String type() {
        return type;
    }

    public String subtype() {
        return subtype;
    }

    @Override
    public String toString() {
        return value;
    }

    public String toString(String charset) {
        return value + "; charset=" + charset;
    }

    public String toString(Charset charset) {
        return toString(charset.name());
    }

}
