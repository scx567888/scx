package cool.scx.common.util;

/**
 * AnnotationUtils
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class AnnotationUtils {

    public static final String NULL = """
            THIS IS A SPECIAL NULL VALUE FOR ANNOTATION - DO NOT USE
            """;

    public static String getAnnotationValue(String value) {
        if (NULL.equals(value)) {
            return null;
        } else {
            return value;
        }
    }

}
