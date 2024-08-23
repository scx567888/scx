package cool.scx.ext.crud;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.field_filter.FieldFilter;
import cool.scx.common.field_filter.serializer.FieldFilterSerializer;
import cool.scx.data.Query;
import cool.scx.data.query.serializer.QuerySerializer;

public class CRUDListParamNew {

    public static final QuerySerializer querySerializer = new QuerySerializer();

    public static final FieldFilterSerializer fieldFilterSerializer = new FieldFilterSerializer();

    public JsonNode query;

    public JsonNode fieldFilter;

    /**
     * 拓展参数
     */
    public JsonNode extParams;

    public Query getQuery() {
        return querySerializer.deserializeQuery(query);
    }

    public FieldFilter getFieldFilter() {
        return fieldFilterSerializer.deserializeFieldFilter(fieldFilter);
    }

}
