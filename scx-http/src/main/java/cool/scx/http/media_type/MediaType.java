package cool.scx.http.media_type;

import cool.scx.http.parameters.Parameters;

/// MediaType
///
/// @author scx567888
/// @version 0.0.1
/// @see <a href="https://www.iana.org/assignments/media-types/media-types.xhtml">https://www.iana.org/assignments/media-types/media-types.xhtml</a>
public enum MediaType implements ScxMediaType {

    // Text
    TEXT_PLAIN("text", "plain"),
    TEXT_HTML("text", "html"),
    TEXT_CSS("text", "css"),
    TEXT_JS("text", "javascript"),

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
    APPLICATION_APK("application", "vnd.android.package-archive"),

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
    private final Parameters<String, String> params;

    MediaType(String type, String subtype) {
        this.type = type;
        this.subtype = subtype;
        this.params = Parameters.of();
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public String subtype() {
        return subtype;
    }

    @Override
    public Parameters<String, String> params() {
        return params;
    }

}
