package cool.scx.http.media_type;

import java.util.HashMap;
import java.util.Map;

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

    private static final Map<String, MediaType> MAP = initMap();
    private final String type;
    private final String subtype;
    private final String value;

    MediaType(String type, String subtype) {
        this.type = type;
        this.subtype = subtype;
        this.value = type + "/" + subtype;
    }

    private static HashMap<String, MediaType> initMap() {
        var map = new HashMap<String, MediaType>();
        for (var h : MediaType.values()) {
            map.put(h.value(), h);
        }
        return map;
    }

    /// @param str s
    /// @return 未找到时抛出异常
    public static MediaType of(String str) {
        var mediaType = find(str);
        if (mediaType == null) {
            throw new IllegalArgumentException("Unknown media type: " + str);
        }
        return mediaType;
    }

    /// @param str s
    /// @return 未找到时返回 null
    public static MediaType find(String str) {
        return MAP.get(str.toLowerCase());
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
    public String value() {
        return value;
    }

}
