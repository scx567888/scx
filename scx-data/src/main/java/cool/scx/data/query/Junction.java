package cool.scx.data.query;

import cool.scx.data.build_control.BuildControl;

import java.util.ArrayList;
import java.util.List;

import static cool.scx.data.query.ConditionType.*;

/// Junction
///
/// @author scx567888
/// @version 0.0.1
public abstract sealed class Junction extends QueryLike<Junction> implements Where permits And, Or {

    private final List<Where> clauses;

    protected Junction() {
        this.clauses = new ArrayList<>();
    }

    public Where[] clauses() {
        return clauses.toArray(Where[]::new);
    }

    public Junction add(Where... logicCauses) {
        for (var logicCause : logicCauses) {
            if (logicCause == null) {
                continue;
            }
            if (logicCause instanceof Condition w && w.info().replace()) {
                clauses.removeIf(c -> c instanceof Condition w1 && w1.selector().equals(w.selector()));
            }
            clauses.add(logicCause);
        }
        return this;
    }

    public Junction clear() {
        clauses.clear();
        return this;
    }

    public final Junction eq(String selector, Object value, BuildControl... options) {
        return add(new Condition(selector, EQ, value, null, options));
    }

    public final Junction ne(String selector, Object value, BuildControl... options) {
        return add(new Condition(selector, NE, value, null, options));
    }

    public final Junction lt(String selector, Object value, BuildControl... options) {
        return add(new Condition(selector, LT, value, null, options));
    }

    public final Junction lte(String selector, Object value, BuildControl... options) {
        return add(new Condition(selector, LTE, value, null, options));
    }

    public final Junction gt(String selector, Object value, BuildControl... options) {
        return add(new Condition(selector, GT, value, null, options));
    }

    public final Junction gte(String selector, Object value, BuildControl... options) {
        return add(new Condition(selector, GTE, value, null, options));
    }

    public final Junction like(String selector, Object value, BuildControl... options) {
        return add(new Condition(selector, LIKE, value, null, options));
    }

    public final Junction notLike(String selector, Object value, BuildControl... options) {
        return add(new Condition(selector, NOT_LIKE, value, null, options));
    }

    public final Junction likeRegex(String selector, String value, BuildControl... options) {
        return add(new Condition(selector, LIKE_REGEX, value, null, options));
    }

    public final Junction notLikeRegex(String selector, String value, BuildControl... options) {
        return add(new Condition(selector, NOT_LIKE_REGEX, value, null, options));
    }

    public final Junction in(String selector, Object value, BuildControl... options) {
        return add(new Condition(selector, IN, value, null, options));
    }

    public final Junction notIn(String selector, Object value, BuildControl... options) {
        return add(new Condition(selector, NOT_IN, value, null, options));
    }

    public final Junction between(String selector, Object value1, Object value2, BuildControl... options) {
        return add(new Condition(selector, BETWEEN, value1, value2, options));
    }

    public final Junction notBetween(String selector, Object value1, Object value2, BuildControl... options) {
        return add(new Condition(selector, NOT_BETWEEN, value1, value2, options));
    }

    public final Junction jsonContains(String selector, Object value, BuildControl... options) {
        return add(new Condition(selector, JSON_CONTAINS, value, null, options));
    }

    public final Junction jsonOverlaps(String selector, Object value, BuildControl... options) {
        return add(new Condition(selector, JSON_OVERLAPS, value, null, options));
    }

    public final Junction and(Where... clauses) {
        return add(new And().add(clauses));
    }

    public final Junction or(Where... clauses) {
        return add(new Or().add(clauses));
    }

    public final Junction not(Where clause) {
        return add(new Not(clause));
    }

    @Override
    protected QueryImpl toQuery() {
        return new QueryImpl().where(this);
    }

}
