package cool.scx.data.query;

import java.util.function.Predicate;

/// Query
///
/// @author scx567888
/// @version 0.0.1
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
    
    Query addWhere(Object... whereClauses);
    
    Query addGroupBy(Object... groupByClauses);

    Query addOrderBy(Object... orderByClauses);

    Query removeWhereIf(Predicate<Object> filter);

    Query removeGroupByIf(Predicate<Object> filter);

    Query removeOrderByIf(Predicate<Object> filter);

}
