package cool.scx.data.query;

import cool.scx.data.build_control.BuildControl;

/// Query
///
/// @author scx567888
/// @version 0.0.1
public interface Query {

    Query where(Object where);

    Query orderBys(OrderBy... orderBys);

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
    
    Query orderBy(OrderBy... orderBys);
    
    Query asc(String name, BuildControl... options);

    Query desc(String name, BuildControl... options);

}
