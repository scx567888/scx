package cool.scx.data.field_filter;

import static cool.scx.data.field_filter.FilterMode.EXCLUDED;
import static cool.scx.data.field_filter.FilterMode.INCLUDED;

/// FieldFilterBuilder
///
/// @author scx567888
/// @version 0.0.1
public class FieldFilterBuilder {

    /// 白名单模式
    public static FieldFilter ofIncluded(String... fieldNames) {
        return new FieldFilterImpl(INCLUDED).addIncluded(fieldNames);
    }

    /// 黑名单模式
    public static FieldFilter ofExcluded(String... fieldNames) {
        return new FieldFilterImpl(EXCLUDED).addExcluded(fieldNames);
    }

}
