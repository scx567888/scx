package cool.scx.sql.exception;

public final class NullInListWhenWhereTypeIsNotNull extends IllegalArgumentException {

    public NullInListWhenWhereTypeIsNotNull() {
        super("当 WhereType 为 NOT_IN 时, 参数列表中不允许任何元素为 null (因为会造成 SQL 查询时无数据以产生歧义) !!!");
    }

}
