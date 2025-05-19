package cool.scx.data.aggregation.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.aggregation.Aggregation;
import cool.scx.data.aggregation.GroupBy;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

    private List<Map<String, Object>> serializeGroupBys(GroupBy[] groupBys) {
        var m = new ArrayList<Map<String, Object>>();
        for (var groupBy : groupBys) {
            m.add(serializeGroupBy0(groupBy));
        }
        return m;
    }

    private Map<String, Object> serializeGroupBy0(GroupBy g) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "GroupBy");
        m.put("name", g.name());
        m.put("expression", g.expression());
        m.put("info", g.info());
        return m;
    }

}
