package cool.scx.data.query.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.data.query.LimitInfo;

import java.util.LinkedHashMap;

public class LimitInfoSerializer {

    public Object serialize(Object obj) {
        return switch (obj) {
            case LimitInfo s -> serializeLimitInfo(s);
            case null, default -> null;
        };
    }

    public LinkedHashMap<String, Object> serializeLimitInfo(LimitInfo limitInfo) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "LimitInfo");
        m.put("offset", limitInfo.getOffset());
        m.put("limit", limitInfo.getLimit());
        return m;
    }

    public Object deserialize(JsonNode v) {
        if (v.isObject()) {
            var type = v.get("@type").asText();
            if (type.equals("LimitInfo")) {
                return deserializeLimitInfo(v);
            }
        }
        return null;
    }

    public LimitInfo deserializeLimitInfo(JsonNode objectNode) {
        if (objectNode == null) {
            return new LimitInfo();
        }
        var limitInfo = new LimitInfo();
        limitInfo.offset(objectNode.get("offset").asLong());
        limitInfo.limit(objectNode.get("limit").asLong());
        return limitInfo;
    }

}
