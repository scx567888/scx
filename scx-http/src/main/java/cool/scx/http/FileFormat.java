package cool.scx.http;

import java.util.Map;
import java.util.TreeMap;

import static cool.scx.http.MediaType.*;
import static java.lang.String.CASE_INSENSITIVE_ORDER;

/**
 * 常见 文件格式 (文件类型/文件后缀/文件拓展名)
 *
 * @author scx567888
 * @version 1.3.4
 */
public enum FileFormat {

    // Text
    TXT(TEXT_PLAIN),
    HTML(TEXT_HTML),
    CSS(TEXT_CSS),
    JS(TEXT_JS),
    MJS(TEXT_JS),

    // Application
    BIN(APPLICATION_OCTET_STREAM),
    XML(APPLICATION_XML),
    JSON(APPLICATION_JSON),
    ZIP(APPLICATION_ZIP),
    PDF(APPLICATION_PDF),
    DOC(APPLICATION_DOC),
    XLS(APPLICATION_XLS),
    PPT(APPLICATION_PPT),
    DOCX(APPLICATION_DOCX),
    XLSX(APPLICATION_XLSX),
    PPTX(APPLICATION_PPTX),
    APK(APPLICATION_APK),

    // Image
    BMP(IMAGE_BMP),
    PNG(IMAGE_PNG),
    JPG(IMAGE_JPEG),
    JPEG(IMAGE_JPEG),
    GIF(IMAGE_GIF),
    SVG(IMAGE_SVG),
    WEBP(IMAGE_WEBP),

    // Audio
    MP3(AUDIO_MPEG),
    WAV(AUDIO_WAV),

    // Video
    MP4(VIDEO_MP4),
    WEBM(VIDEO_WEBM);

    private static final Map<String, FileFormat> MAP = initMAP();

    private final MediaType mediaType;

    FileFormat(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    private static Map<String, FileFormat> initMAP() {
        var map = new TreeMap<String, FileFormat>(CASE_INSENSITIVE_ORDER);
        for (var value : FileFormat.values()) {
            map.put(value.name(), value);
        }
        return map;
    }

    public static FileFormat findByExtension(String ext) {
        return MAP.get(ext);
    }

    public static FileFormat findByFileName(String filename) {
        int li = filename.lastIndexOf('.');
        if (li != -1 && li != filename.length() - 1) {
            var ext = filename.substring(li + 1);
            return findByExtension(ext);
        }
        return null;
    }

    /**
     * 对应的 mediaType
     *
     * @return mediaType
     */
    public MediaType mediaType() {
        return mediaType;
    }

}
