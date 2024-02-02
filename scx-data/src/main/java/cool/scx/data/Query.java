package cool.scx.data;

import cool.scx.data.query.GroupBy;
import cool.scx.data.query.LimitInfo;
import cool.scx.data.query.OrderBy;
import cool.scx.data.query.Where;

public interface Query {

    Query where(Object... whereClauses);

    Query groupBy(Object... groupByClauses);

    Query orderBy(Object... orderByClauses);

    Query addWhere(Object... whereClauses);

    Query addGroupBy(Object... groupByClauses);

    Query addOrderBy(Object... orderByClauses);

    Query offset(long limitOffset);

    Query limit(long numberOfRows);

    Where getWhere();

    GroupBy getGroupBy();

    OrderBy getOrderBy();

    Long getOffset();

    Long getLimit();

    LimitInfo getLimitInfo();

    Query clearWhere();

    Query clearGroupBy();

    Query clearOrderBy();

    Query clearOffset();

    Query clearLimit();

}
