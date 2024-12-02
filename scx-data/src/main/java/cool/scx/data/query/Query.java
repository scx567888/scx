package cool.scx.data.query;

/**
 * Query
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface Query {

    Query where(Object... whereClauses);

    Query groupBy(Object... groupByClauses);

    Query orderBy(Object... orderByClauses);

    Query offset(long limitOffset);

    Query limit(long numberOfRows);

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
