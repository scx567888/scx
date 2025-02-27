package cool.scx.common.constant;

import static cool.scx.common.constant.AnnotationValue.NULL;

/// AnnotationValueHelper
///
/// @author scx567888
/// @version 0.0.1
public final class AnnotationValueHelper {

    public static String getRealValue(String value) {
        return NULL.equals(value) ? null : value;
    }

}
