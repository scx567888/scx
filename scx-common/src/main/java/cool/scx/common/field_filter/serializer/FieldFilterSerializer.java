package cool.scx.common.field_filter.serializer;

import cool.scx.common.field_filter.FieldFilter;

import java.util.LinkedHashMap;

public class FieldFilterSerializer {

    public static final FieldFilterSerializer FIELD_FILTER_SERIALIZER = new FieldFilterSerializer();

    public Object serialize(Object obj) {
        return switch (obj) {
            case FieldFilter s -> serializeFieldFilter(s);
            case null, default -> null;
        };
    }

    public LinkedHashMap<String, Object> serializeFieldFilter(FieldFilter fieldFilter) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "FieldFilter");
        m.put("filterMode", fieldFilter.getFilterMode());
        m.put("fieldNames", fieldFilter.getFieldNames());
        m.put("ignoreNullValue", fieldFilter.getIgnoreNullValue());
        return m;
    }

}
