package cool.scx.data.query.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.data.query.LimitInfo;

import java.util.LinkedHashMap;

public class LimitInfoSerializer {

    public LinkedHashMap<String, Object> serialize(LimitInfo limitInfo) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "LimitInfo");
        m.put("offset", limitInfo.getOffset());
        m.put("limit", limitInfo.getLimit());
        return m;
    }

    public LimitInfo deserializeLimitInfo(JsonNode objectNode) {
        var limitInfo = new LimitInfo();
        limitInfo.offset(objectNode.get("offset").asLong());
        limitInfo.limit(objectNode.get("limit").asLong());
        return limitInfo;
    }

}
