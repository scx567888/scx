package cool.scx.data.query;

import java.util.ArrayList;
import java.util.List;

import static cool.scx.data.query.WhereType.*;
import static java.util.Collections.addAll;

public class Logic extends QueryLike<Logic> {

    private final LogicType logicType;
    private final List<Object> clauses;

    public Logic(LogicType logicType) {
        this.logicType = logicType;
        this.clauses = new ArrayList<>();
    }

    public LogicType logicType() {
        return logicType;
    }

    public Object[] clauses() {
        return clauses.toArray();
    }

    public Logic add(Object... logicCauses) {
        addAll(clauses, logicCauses);
        return this;
    }

    public final Logic addWhere(String name, WhereType whereType, Object value1, Object value2, QueryOption... options) {
        return add(new Where(name, whereType, value1, value2, options));
    }

    public final Logic eq(String fieldName, Object value, QueryOption... options) {
        return addWhere(fieldName, EQUAL, value, null, options);
    }

    public final Logic ne(String fieldName, Object value, QueryOption... options) {
        return addWhere(fieldName, NOT_EQUAL, value, null, options);
    }

    public final Logic lt(String fieldName, Object value, QueryOption... options) {
        return addWhere(fieldName, LESS_THAN, value, null, options);
    }

    public final Logic le(String fieldName, Object value, QueryOption... options) {
        return addWhere(fieldName, LESS_THAN_OR_EQUAL, value, null, options);
    }

    public final Logic gt(String fieldName, Object value, QueryOption... options) {
        return addWhere(fieldName, GREATER_THAN, value, null, options);
    }

    public final Logic ge(String fieldName, Object value, QueryOption... options) {
        return addWhere(fieldName, GREATER_THAN_OR_EQUAL, value, null, options);
    }

    public final Logic isNull(String fieldName, QueryOption... options) {
        return addWhere(fieldName, IS_NULL, null, null, options);
    }

    public final Logic isNotNull(String fieldName, QueryOption... options) {
        return addWhere(fieldName, IS_NOT_NULL, null, null, options);
    }

    public final Logic like(String fieldName, Object value, QueryOption... options) {
        return addWhere(fieldName, LIKE, value, null, options);
    }

    public final Logic notLike(String fieldName, Object value, QueryOption... options) {
        return addWhere(fieldName, NOT_LIKE, value, null, options);
    }

    public final Logic likeRegex(String fieldName, String value, QueryOption... options) {
        return addWhere(fieldName, LIKE_REGEX, value, null, options);
    }

    public final Logic notLikeRegex(String fieldName, String value, QueryOption... options) {
        return addWhere(fieldName, NOT_LIKE_REGEX, value, null, options);
    }

    public final Logic in(String fieldName, Object value, QueryOption... options) {
        return addWhere(fieldName, IN, value, null, options);
    }

    public final Logic notIn(String fieldName, Object value, QueryOption... options) {
        return addWhere(fieldName, NOT_IN, value, null, options);
    }

    public final Logic between(String fieldName, Object value1, Object value2, QueryOption... options) {
        return addWhere(fieldName, BETWEEN, value1, value2, options);
    }

    public final Logic notBetween(String fieldName, Object value1, Object value2, QueryOption... options) {
        return addWhere(fieldName, NOT_BETWEEN, value1, value2, options);
    }

    public final Logic jsonContains(String fieldName, Object value, QueryOption... options) {
        return addWhere(fieldName, JSON_CONTAINS, value, null, options);
    }

    @Override
    protected Query toQuery() {
        return new QueryImpl().where(this);
    }

}
