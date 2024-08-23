package cool.scx.common.field_filter.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.field_filter.ExcludedFieldFilter;
import cool.scx.common.field_filter.FieldFilter;
import cool.scx.common.field_filter.FilterMode;
import cool.scx.common.field_filter.IncludedFieldFilter;
import cool.scx.common.util.ObjectUtils;

public class FieldFilterDeserializer {

    public static final FieldFilterDeserializer FIELD_FILTER_DESERIALIZER = new FieldFilterDeserializer();

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
        if (objectNode == null) {
            return FieldFilter.ofExcluded();
        }
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
