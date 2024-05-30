package cool.scx.common.util;

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
