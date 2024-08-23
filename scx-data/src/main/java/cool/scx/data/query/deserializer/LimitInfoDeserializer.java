package cool.scx.data.query.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.data.query.LimitInfo;

public class LimitInfoDeserializer {

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
