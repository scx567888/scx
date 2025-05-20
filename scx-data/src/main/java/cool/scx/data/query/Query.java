package cool.scx.data.query;

/// Query
///
/// @author scx567888
/// @version 0.0.1
public interface Query {

    Query where(Object where);

    Query orderBy(OrderBy... orderBys);

    Query offset(long offset);

    Query limit(long limit);

    Object getWhere();

    OrderBy[] getOrderBys();

    Long getOffset();

    Long getLimit();

    Query clearWhere();

    Query clearOrderBys();

    Query clearOffset();

    Query clearLimit();

}
