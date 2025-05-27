package cool.scx.data.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.aggregation.*;

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
        m.put("groupBys", serializeGroupBys(aggregation.getGroupBys()));
        m.put("aggs", serializeAggs(aggregation.getAggs()));
        return m;
    }

    private List<Map<String, Object>> serializeGroupBys(GroupBy[] groupBys) {
        var m = new ArrayList<Map<String, Object>>();
        for (var groupBy : groupBys) {
            m.add(serializeGroupBy(groupBy));
        }
        return m;
    }

    private Map<String, Object> serializeGroupBy(GroupBy g) {
        return switch (g) {
            case FieldGroupBy fieldGroupBy -> serializeFieldGroupBy(fieldGroupBy);
            case ExpressionGroupBy fieldGroupBy -> serializeExpressionGroupBy(fieldGroupBy);
        };
    }

    public Map<String, Object> serializeFieldGroupBy(FieldGroupBy groupBy) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "FieldGroupBy");
        m.put("fieldName", groupBy.fieldName());
        return m;
    }

    public Map<String, Object> serializeExpressionGroupBy(ExpressionGroupBy groupBy) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "ExpressionGroupBy");
        m.put("alias", groupBy.alias());
        m.put("expression", groupBy.expression());
        return m;
    }

    private List<Map<String, Object>> serializeAggs(Agg[] aggs) {
        var m = new ArrayList<Map<String, Object>>();
        for (var agg : aggs) {
            m.add(serializeAgg(agg));
        }
        return m;
    }

    private Map<String, Object> serializeAgg(Agg g) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "Agg");
        m.put("alias", g.alias());
        m.put("expression", g.expression());
        return m;
    }

}
