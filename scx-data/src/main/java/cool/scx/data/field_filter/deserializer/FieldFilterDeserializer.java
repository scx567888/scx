package cool.scx.data.field_filter.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.FieldFilter;
import cool.scx.data.field_filter.ExcludedFieldFilter;
import cool.scx.data.field_filter.FilterMode;
import cool.scx.data.field_filter.IncludedFieldFilter;

import static cool.scx.data.field_filter.FilterMode.EXCLUDED;
import static cool.scx.data.field_filter.FilterMode.INCLUDED;

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
            return new ExcludedFieldFilter();
        }
        var filterMode = FilterMode.of(objectNode.get("filterMode").textValue());
        var fieldNames = ObjectUtils.convertValue(objectNode.get("fieldNames"), String[].class);
        var ignoreNullValue = objectNode.get("ignoreNullValue").asBoolean();
        if (filterMode == INCLUDED) {
            return new IncludedFieldFilter().addIncluded(fieldNames).ignoreNullValue(ignoreNullValue);
        } else if (filterMode == EXCLUDED) {
            return new ExcludedFieldFilter().addIncluded(fieldNames).ignoreNullValue(ignoreNullValue);
        } else {
            return null;
        }
    }

}
