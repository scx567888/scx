package cool.scx.data.aggregation.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.aggregation.Aggregation;
import cool.scx.data.aggregation.GroupBy;

import java.util.LinkedHashMap;
import java.util.Map;

public class AggregationSerializer {

    public static final AggregationSerializer AGGREGATION_DEFINITION_SERIALIZER = new AggregationSerializer();

    public String toJson(Aggregation aggregation) throws JsonProcessingException {
        var v = serialize(aggregation);
        return ObjectUtils.jsonMapper().writeValueAsString(v);
    }

    public Object serialize(Aggregation aggregation) {
        return serializeAggregationDefinition(aggregation);
    }

    public Map<String, Object> serializeAggregationDefinition(Aggregation aggregation) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "Aggregation");
        m.put("groupBys", serializeGroupBys(aggregation.groupBys()));
        m.put("aggs", aggregation.aggs());
        return m;
    }

    public Map<String, Object> serializeGroupBys(Map<String, GroupBy> groupBys) {
        var m = new LinkedHashMap<String, Object>();
        for (var entry : groupBys.entrySet()) {
            m.put(entry.getKey(), serializeGroupBy(entry.getValue()));
        }
        return m;
    }

    public Object serializeGroupBy(Object obj) {
        return switch (obj) {
            case String s -> serializeString(s);
            case GroupBy g -> serializeGroupBy0(g);
            case Object[] o -> serializeGroupByAll(o);
            default -> obj;
        };
    }

    private Object serializeString(String s) {
        return s;
    }

    private Object serializeGroupBy0(GroupBy g) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "GroupBy");
        m.put("name", g.name());
        m.put("expression", g.expression());
        m.put("info", g.info());
        return m;
    }

    private Object[] serializeGroupByAll(Object[] objs) {
        var arr = new Object[objs.length];
        for (int i = 0; i < objs.length; i = i + 1) {
            arr[i] = serializeGroupBy(objs[i]);
        }
        return arr;
    }

}
