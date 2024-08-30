package cool.scx.data.query;

import cool.scx.data.q.Where;

import static cool.scx.data.query.WhereType.*;

public interface Logic {

    LogicType logicType();

    Object[] clauses();

    Logic add(Object... logicCauses);

    default Logic addWhere(String name, WhereType whereType, Object value1, Object value2, WhereOption... options) {
        return add(new Where(name, whereType, value1, value2, options));
    }

    default Logic eq(String fieldName, Object value, WhereOption... options) {
        return addWhere(fieldName, EQUAL, value, null, options);
    }

    default Logic ne(String fieldName, Object value, WhereOption... options) {
        return addWhere(fieldName, NOT_EQUAL, value, null, options);
    }

    default Logic lt(String fieldName, Object value, WhereOption... options) {
        return addWhere(fieldName, LESS_THAN, value, null, options);
    }

    default Logic le(String fieldName, Object value, WhereOption... options) {
        return addWhere(fieldName, LESS_THAN_OR_EQUAL, value, null, options);
    }

    default Logic gt(String fieldName, Object value, WhereOption... options) {
        return addWhere(fieldName, GREATER_THAN, value, null, options);
    }

    default Logic ge(String fieldName, Object value, WhereOption... options) {
        return addWhere(fieldName, GREATER_THAN_OR_EQUAL, value, null, options);
    }

    default Logic isNull(String fieldName, WhereOption... options) {
        return addWhere(fieldName, IS_NULL, null, null, options);
    }

    default Logic isNotNull(String fieldName, WhereOption... options) {
        return addWhere(fieldName, IS_NOT_NULL, null, null, options);
    }

    default Logic like(String fieldName, Object value, WhereOption... options) {
        return addWhere(fieldName, LIKE, value, null, options);
    }

    default Logic notLike(String fieldName, Object value, WhereOption... options) {
        return addWhere(fieldName, NOT_LIKE, value, null, options);
    }

    default Logic likeRegex(String fieldName, String value, WhereOption... options) {
        return addWhere(fieldName, LIKE_REGEX, value, null, options);
    }

    default Logic notLikeRegex(String fieldName, String value, WhereOption... options) {
        return addWhere(fieldName, NOT_LIKE_REGEX, value, null, options);
    }

    default Logic in(String fieldName, Object value, WhereOption... options) {
        return addWhere(fieldName, IN, value, null, options);
    }

    default Logic notIn(String fieldName, Object value, WhereOption... options) {
        return addWhere(fieldName, NOT_IN, value, null, options);
    }

    default Logic between(String fieldName, Object value1, Object value2, WhereOption... options) {
        return addWhere(fieldName, BETWEEN, value1, value2, options);
    }

    default Logic notBetween(String fieldName, Object value1, Object value2, WhereOption... options) {
        return addWhere(fieldName, NOT_BETWEEN, value1, value2, options);
    }

    default Logic jsonContains(String fieldName, Object value, WhereOption... options) {
        return addWhere(fieldName, JSON_CONTAINS, value, null, options);
    }

}
