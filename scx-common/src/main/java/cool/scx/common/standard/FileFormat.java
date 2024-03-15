package cool.scx.common.standard;

import java.util.Map;
import java.util.TreeMap;

import static java.lang.String.CASE_INSENSITIVE_ORDER;

/**
 * 常见 文件格式 (文件类型/文件后缀/文件拓展名)
 *
 * @author scx567888
 * @version 1.3.4
 */
public enum FileFormat {

    // Text
    TXT(MediaType.TEXT_PLAIN),
    HTML(MediaType.TEXT_HTML),

    // Application
    BIN(MediaType.APPLICATION_OCTET_STREAM),
    XML(MediaType.APPLICATION_XML),
    JSON(MediaType.APPLICATION_JSON),
    ZIP(MediaType.APPLICATION_ZIP),
    PDF(MediaType.APPLICATION_PDF),
    DOC(MediaType.APPLICATION_DOC),
    XLS(MediaType.APPLICATION_XLS),
    PPT(MediaType.APPLICATION_PPT),
    DOCX(MediaType.APPLICATION_DOCX),
    XLSX(MediaType.APPLICATION_XLSX),
    PPTX(MediaType.APPLICATION_PPTX),

    // Image
    BMP(MediaType.IMAGE_BMP),
    PNG(MediaType.IMAGE_PNG),
    JPEG(MediaType.IMAGE_JPEG),
    GIF(MediaType.IMAGE_GIF),
    SVG(MediaType.IMAGE_SVG),
    WEBP(MediaType.IMAGE_WEBP),

    // Audio
    MP3(MediaType.AUDIO_MPEG),
    WAV(MediaType.AUDIO_WAV),

    // Video
    MP4(MediaType.VIDEO_MP4),
    WEBM(MediaType.VIDEO_WEBM);

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

    public static FileFormat ofExtension(String ext) {
        return MAP.get(ext);
    }

    public static FileFormat ofFileName(String filename) {
        int li = filename.lastIndexOf('.');
        if (li != -1 && li != filename.length() - 1) {
            var ext = filename.substring(li + 1);
            return ofExtension(ext);
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
