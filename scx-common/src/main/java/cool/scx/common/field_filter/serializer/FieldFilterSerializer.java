package cool.scx.common.field_filter.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.util.ObjectUtils;
import cool.scx.common.field_filter.FieldFilter;
import cool.scx.common.field_filter.ExcludedFieldFilter;
import cool.scx.common.field_filter.FilterMode;
import cool.scx.common.field_filter.IncludedFieldFilter;

import java.util.LinkedHashMap;

public class FieldFilterSerializer {

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

    public Object deserialize(JsonNode v) {
        if (v.isObject()) {
            var type = v.get("@type").asText();
            if (type.equals("FieldFilter")) {
                return deserializeFieldFilter(v);
            }
        }
        return null;
    }

    public FieldFilter deserializeFieldFilter(JsonNode objectNode) {
        var filterMode = FilterMode.of(objectNode.get("filterMode").textValue());
        var fieldNames = ObjectUtils.convertValue(objectNode.get("fieldNames"), String[].class);
        var ignoreNullValue = objectNode.get("ignoreNullValue").asBoolean();
        if (filterMode == FilterMode.INCLUDED) {
            return new IncludedFieldFilter().addIncluded(fieldNames).ignoreNullValue(ignoreNullValue);
        } else if (filterMode == FilterMode.EXCLUDED) {
            return new ExcludedFieldFilter().addIncluded(fieldNames).ignoreNullValue(ignoreNullValue);
        } else {
            return null;
        }
    }

}
