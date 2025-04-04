package cool.scx.data.query;

import java.util.ArrayList;
import java.util.List;

import static cool.scx.data.query.WhereType.*;

/// Logic
///
/// @author scx567888
/// @version 0.0.1
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
        for (var logicCause : logicCauses) {
            if (logicCause == null) {
                continue;
            }
            if (logicCause instanceof Object[] objs) {
                add(objs);
                continue;
            }
            if (logicCause instanceof Where w && w.info().replace()) {
                clauses.removeIf(c -> c instanceof Where w1 && w1.name().equals(w.name()));
            }
            clauses.add(logicCause);
        }
        return this;
    }

    public Logic clear() {
        clauses.clear();
        return this;
    }

    public final Logic eq(String fieldName, Object value, QueryOption... options) {
        return add(new Where(fieldName, EQUAL, value, null, options));
    }

    public final Logic ne(String fieldName, Object value, QueryOption... options) {
        return add(new Where(fieldName, NOT_EQUAL, value, null, options));
    }

    public final Logic lt(String fieldName, Object value, QueryOption... options) {
        return add(new Where(fieldName, LESS_THAN, value, null, options));
    }

    public final Logic le(String fieldName, Object value, QueryOption... options) {
        return add(new Where(fieldName, LESS_THAN_OR_EQUAL, value, null, options));
    }

    public final Logic gt(String fieldName, Object value, QueryOption... options) {
        return add(new Where(fieldName, GREATER_THAN, value, null, options));
    }

    public final Logic ge(String fieldName, Object value, QueryOption... options) {
        return add(new Where(fieldName, GREATER_THAN_OR_EQUAL, value, null, options));
    }

    public final Logic isNull(String fieldName, QueryOption... options) {
        return add(new Where(fieldName, IS_NULL, null, null, options));
    }

    public final Logic isNotNull(String fieldName, QueryOption... options) {
        return add(new Where(fieldName, IS_NOT_NULL, null, null, options));
    }

    public final Logic like(String fieldName, Object value, QueryOption... options) {
        return add(new Where(fieldName, LIKE, value, null, options));
    }

    public final Logic notLike(String fieldName, Object value, QueryOption... options) {
        return add(new Where(fieldName, NOT_LIKE, value, null, options));
    }

    public final Logic likeRegex(String fieldName, String value, QueryOption... options) {
        return add(new Where(fieldName, LIKE_REGEX, value, null, options));
    }

    public final Logic notLikeRegex(String fieldName, String value, QueryOption... options) {
        return add(new Where(fieldName, NOT_LIKE_REGEX, value, null, options));
    }

    public final Logic in(String fieldName, Object value, QueryOption... options) {
        return add(new Where(fieldName, IN, value, null, options));
    }

    public final Logic notIn(String fieldName, Object value, QueryOption... options) {
        return add(new Where(fieldName, NOT_IN, value, null, options));
    }

    public final Logic between(String fieldName, Object value1, Object value2, QueryOption... options) {
        return add(new Where(fieldName, BETWEEN, value1, value2, options));
    }

    public final Logic notBetween(String fieldName, Object value1, Object value2, QueryOption... options) {
        return add(new Where(fieldName, NOT_BETWEEN, value1, value2, options));
    }

    public final Logic jsonContains(String fieldName, Object value, QueryOption... options) {
        return add(new Where(fieldName, JSON_CONTAINS, value, null, options));
    }

    public final Logic jsonOverlaps(String fieldName, Object value, QueryOption... options) {
        return add(new Where(fieldName, JSON_OVERLAPS, value, null, options));
    }

    @Override
    protected Query toQuery() {
        return new QueryImpl().where(this);
    }

}
