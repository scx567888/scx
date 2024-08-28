package cool.scx.ext.crud;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.field_filter.FieldFilter;
import cool.scx.data.Query;

import static cool.scx.common.field_filter.deserializer.FieldFilterDeserializer.FIELD_FILTER_DESERIALIZER;
import static cool.scx.data.query.deserializer.QueryDeserializer.QUERY_DESERIALIZER;

public class CRUDListParam {

    public JsonNode query;

    public JsonNode fieldFilter;

    /**
     * 拓展参数
     */
    public JsonNode extParams;

    public Query getQuery() {
        return QUERY_DESERIALIZER.deserializeQuery(query);
    }

    public FieldFilter getFieldFilter() {
        return FIELD_FILTER_DESERIALIZER.deserializeFieldFilter(fieldFilter);
    }

}
