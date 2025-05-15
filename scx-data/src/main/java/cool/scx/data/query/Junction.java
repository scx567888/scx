package cool.scx.data.query;

import java.util.ArrayList;
import java.util.List;

import static cool.scx.data.query.WhereType.*;

/// Junction
///
/// @author scx567888
/// @version 0.0.1
public abstract sealed class Junction extends QueryLike<Junction> permits And, Or {

    private final List<Object> clauses;

    protected Junction() {
        this.clauses = new ArrayList<>();
    }

    public Object[] clauses() {
        return clauses.toArray();
    }

    public Junction add(Object... logicCauses) {
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

    public Junction clear() {
        clauses.clear();
        return this;
    }

    public final Junction eq(String fieldName, Object value, QueryOption... options) {
        return add(new Where(fieldName, EQ, value, null, options));
    }

    public final Junction ne(String fieldName, Object value, QueryOption... options) {
        return add(new Where(fieldName, NE, value, null, options));
    }

    public final Junction lt(String fieldName, Object value, QueryOption... options) {
        return add(new Where(fieldName, LT, value, null, options));
    }

    public final Junction lte(String fieldName, Object value, QueryOption... options) {
        return add(new Where(fieldName, LTE, value, null, options));
    }

    public final Junction gt(String fieldName, Object value, QueryOption... options) {
        return add(new Where(fieldName, GT, value, null, options));
    }

    public final Junction gte(String fieldName, Object value, QueryOption... options) {
        return add(new Where(fieldName, GTE, value, null, options));
    }

    public final Junction like(String fieldName, Object value, QueryOption... options) {
        return add(new Where(fieldName, LIKE, value, null, options));
    }

    public final Junction notLike(String fieldName, Object value, QueryOption... options) {
        return add(new Where(fieldName, NOT_LIKE, value, null, options));
    }

    public final Junction likeRegex(String fieldName, String value, QueryOption... options) {
        return add(new Where(fieldName, LIKE_REGEX, value, null, options));
    }

    public final Junction notLikeRegex(String fieldName, String value, QueryOption... options) {
        return add(new Where(fieldName, NOT_LIKE_REGEX, value, null, options));
    }

    public final Junction in(String fieldName, Object value, QueryOption... options) {
        return add(new Where(fieldName, IN, value, null, options));
    }

    public final Junction notIn(String fieldName, Object value, QueryOption... options) {
        return add(new Where(fieldName, NOT_IN, value, null, options));
    }

    public final Junction between(String fieldName, Object value1, Object value2, QueryOption... options) {
        return add(new Where(fieldName, BETWEEN, value1, value2, options));
    }

    public final Junction notBetween(String fieldName, Object value1, Object value2, QueryOption... options) {
        return add(new Where(fieldName, NOT_BETWEEN, value1, value2, options));
    }

    public final Junction jsonContains(String fieldName, Object value, QueryOption... options) {
        return add(new Where(fieldName, JSON_CONTAINS, value, null, options));
    }

    public final Junction jsonOverlaps(String fieldName, Object value, QueryOption... options) {
        return add(new Where(fieldName, JSON_OVERLAPS, value, null, options));
    }

    public final Junction and(Object... clauses) {
        return add(new And().add(clauses));
    }

    public final Junction or(Object... clauses) {
        return add(new Or().add(clauses));
    }

    public final Junction not(Object clause) {
        return add(new Not(clause));
    }

    @Override
    protected Query toQuery() {
        return new QueryImpl().where(this);
    }

}
