package cool.scx.data.query;

/// Query
///
/// @author scx567888
/// @version 0.0.1
public interface Query {

    Query where(Object where);

    Query orderBy(Object... orderByClauses);

    Query offset(long offset);

    Query limit(long limit);

    Object getWhere();

    Object[] getOrderBy();

    Long getOffset();

    Long getLimit();

    Query clearWhere();

    Query clearOrderBy();

    Query clearOffset();

    Query clearLimit();

    Query addOrderBy(Object... orderByClauses);

    Query removeOrderBy(String fieldName);

}
