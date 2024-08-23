package cool.scx.data.query.serializer;

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

}
