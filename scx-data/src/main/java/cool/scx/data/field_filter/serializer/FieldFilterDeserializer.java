package cool.scx.data.field_filter.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.data.FieldFilter;
import cool.scx.data.field_filter.ExcludedFieldFilter;
import cool.scx.data.field_filter.FilterMode;
import cool.scx.data.field_filter.IncludedFieldFilter;

import static cool.scx.common.util.ObjectUtils.convertValue;

public class FieldFilterDeserializer {

    public static final FieldFilterDeserializer FIELD_FILTER_DESERIALIZER = new FieldFilterDeserializer();

    public Object deserialize(JsonNode v) {
        if (v.isObject()) {
            var type = v.get("@type").asText();
            if (type.equals("FieldFilter")) {
                return deserializeFieldFilter(v);
            }
        }
        return v;
    }

    public FieldFilter deserializeFieldFilter(JsonNode objectNode) {
        if (objectNode == null) {
            return new ExcludedFieldFilter();
        }
        var filterMode = convertValue(objectNode.get("filterMode"), FilterMode.class);
        var fieldNames = convertValue(objectNode.get("fieldNames"), String[].class);
        var ignoreNullValue = objectNode.get("ignoreNullValue").asBoolean();
        return switch (filterMode) {
            case INCLUDED -> new IncludedFieldFilter().addIncluded(fieldNames).ignoreNullValue(ignoreNullValue);
            case EXCLUDED -> new ExcludedFieldFilter().addExcluded(fieldNames).ignoreNullValue(ignoreNullValue);
        };
    }

}
