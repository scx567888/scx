package cool.scx.data.query.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.query.Query;

import java.util.LinkedHashMap;

/**
 * QuerySerializer
 *
 * @author scx567888
 * @version 0.0.1
 */
public class QuerySerializer {

    public static final QuerySerializer QUERY_SERIALIZER = new QuerySerializer();

    private final WhereSerializer whereSerializer;
    private final GroupBySerializer groupBySerializer;
    private final OrderBySerializer orderBySerializer;

    public QuerySerializer() {
        this.whereSerializer = new WhereSerializer();
        this.groupBySerializer = new GroupBySerializer();
        this.orderBySerializer = new OrderBySerializer();
    }

    public String toJson(Query query) throws JsonProcessingException {
        var v = serialize(query);
        return ObjectUtils.jsonMapper().writeValueAsString(v);
    }

    public Object serialize(Object obj) {
        return switch (obj) {
            case Query s -> serializeQuery(s);
            default -> obj;
        };
    }

    private Object serializeQuery(Query query) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "Query");
        m.put("where", whereSerializer.serialize(query));
        m.put("groupBy", groupBySerializer.serialize(query));
        m.put("orderBy", orderBySerializer.serialize(query));
        m.put("offset", query.getOffset());
        m.put("limit", query.getLimit());
        return m;
    }

}
