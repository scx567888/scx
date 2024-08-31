package cool.scx.data.query;

public interface Query {

    Query where(Object... whereClauses);

    Query groupBy(Object... groupByClauses);

    Query orderBy(Object... orderByClauses);

    Query offset(long limitOffset);

    Query limit(long numberOfRows);

    Query addWhere(Object... whereClauses);

    Query addGroupBy(Object... groupByClauses);

    Query addOrderBy(Object... orderByClauses);

    Object[] getWhere();

    Object[] getGroupBy();

    Object[] getOrderBy();

    Long getOffset();

    Long getLimit();

    Query clearWhere();

    Query clearGroupBy();

    Query clearOrderBy();

    Query clearOffset();

    Query clearLimit();

}
