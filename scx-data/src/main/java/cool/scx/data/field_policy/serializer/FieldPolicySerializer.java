package cool.scx.data.field_policy.serializer;

import cool.scx.data.field_policy.FieldPolicy;

import java.util.LinkedHashMap;

/// FieldFilterDeserializer
///
/// @author scx567888
/// @version 0.0.1
public class FieldPolicySerializer {

    public static final FieldPolicySerializer FIELD_FILTER_SERIALIZER = new FieldPolicySerializer();

    public Object serialize(Object obj) {
        return switch (obj) {
            case FieldPolicy s -> serializeFieldFilter(s);
            default -> obj;
        };
    }

    public LinkedHashMap<String, Object> serializeFieldFilter(FieldPolicy fieldFilter) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "FieldFilter");
        m.put("filterMode", fieldFilter.getFilterMode());
        m.put("fieldNames", fieldFilter.getFieldNames());
        m.put("ignoreNullValue", fieldFilter.getIgnoreNullValue());
        return m;
    }

}
