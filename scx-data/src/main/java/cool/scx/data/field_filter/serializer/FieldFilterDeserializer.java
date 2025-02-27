package cool.scx.data.field_filter.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.data.field_filter.FieldFilter;
import cool.scx.data.field_filter.FieldFilterImpl;
import cool.scx.data.field_filter.FilterMode;

import static cool.scx.common.util.ObjectUtils.convertValue;
import static cool.scx.data.field_filter.FilterMode.EXCLUDED;

/// FieldFilterDeserializer
///
/// @author scx567888
/// @version 0.0.1
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
        var filterMode = EXCLUDED;

        if (objectNode == null) {
            return new FieldFilterImpl(filterMode);
        }

        if (objectNode.get("filterMode") != null && !objectNode.get("filterMode").isNull()) {
            filterMode = convertValue(objectNode.get("filterMode"), FilterMode.class);
        }

        var fieldFilter = new FieldFilterImpl(filterMode);

        if (objectNode.get("fieldNames") != null && !objectNode.get("fieldNames").isNull()) {
            var fieldNames = convertValue(objectNode.get("fieldNames"), String[].class);
            fieldFilter.addFieldNames(fieldNames);
        }

        if (objectNode.get("ignoreNullValue") != null && !objectNode.get("ignoreNullValue").isNull()) {
            var ignoreNullValue = objectNode.get("ignoreNullValue").asBoolean();
            fieldFilter.ignoreNullValue(ignoreNullValue);
        }

        return fieldFilter;
    }

}
