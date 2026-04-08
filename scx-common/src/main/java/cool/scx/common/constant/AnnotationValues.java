package cool.scx.common.constant;

/// AnnotationValues
///
/// @author scx567888
/// @version 0.0.1
public final class AnnotationValues {

    public static final String NULL = """
            THIS IS A SPECIAL NULL VALUE FOR ANNOTATION - DO NOT USE
            """;

    public static String getRealValue(String value) {
        return NULL.equals(value) ? null : value;
    }

}
