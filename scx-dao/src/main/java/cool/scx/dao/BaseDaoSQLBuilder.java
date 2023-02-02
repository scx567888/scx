package cool.scx.dao;

import cool.scx.dao.group_by.GroupBy;
import cool.scx.dao.order_by.OrderBy;
import cool.scx.dao.pagination.Pagination;
import cool.scx.dao.where.Where;
import cool.scx.sql.ColumnInfo;
import cool.scx.sql.SQLBuilder;

import java.util.Arrays;

final class BaseDaoSQLBuilder {

    private final SQLBuilder sqlBuilder;

    public BaseDaoSQLBuilder(SQLBuilder sqlBuilder) {
        this.sqlBuilder = sqlBuilder;
    }

    public static BaseDaoSQLBuilder Select(String... selectColumns) {
        return new BaseDaoSQLBuilder(SQLBuilder.Select(selectColumns));
    }

    public static BaseDaoSQLBuilder Insert(String tableName, ColumnInfo... insertColumnInfos) {
        return new BaseDaoSQLBuilder(SQLBuilder.Insert(tableName, Arrays.stream(insertColumnInfos).map(ColumnInfo::columnName).toArray(String[]::new)));
    }

    public static BaseDaoSQLBuilder Update(String tableName) {
        return new BaseDaoSQLBuilder(SQLBuilder.Update(tableName));
    }

    public static BaseDaoSQLBuilder Delete(String tableName) {
        return new BaseDaoSQLBuilder(SQLBuilder.Delete(tableName));
    }

    public static BaseDaoSQLBuilder Select(BaseDaoColumnInfo[] selectColumnInfos) {
        return new BaseDaoSQLBuilder(SQLBuilder.Select(Arrays.stream(selectColumnInfos).map(BaseDaoColumnInfo::selectSQL).toArray(String[]::new)));
    }

    public BaseDaoSQLBuilder Values(BaseDaoColumnInfo... insertColumnInfos) {
        sqlBuilder.Values(Arrays.stream(insertColumnInfos).map(BaseDaoColumnInfo::insertValuesSQL).toArray(String[]::new));
        return this;
    }

    public String GetSQL() {
        return sqlBuilder.GetSQL();
    }

    public BaseDaoSQLBuilder From(String tableName) {
        this.sqlBuilder.From(tableName);
        return this;
    }

    public BaseDaoSQLBuilder Where(Where where) {
        if (where != null) {
            this.sqlBuilder.Where(where.getWhereClauses());
        }
        return this;
    }

    public BaseDaoSQLBuilder GroupBy(GroupBy groupBy) {
        if (groupBy != null) {
            this.sqlBuilder.GroupBy(groupBy.getGroupByColumns());
        }
        return this;
    }

    public BaseDaoSQLBuilder OrderBy(OrderBy orderBy) {
        if (orderBy != null) {
            this.sqlBuilder.OrderBy(orderBy.getOrderByClauses());
        }
        return this;
    }

    public BaseDaoSQLBuilder Limit(Pagination pagination) {
        if (pagination != null) {
            this.sqlBuilder.Limit(pagination.offset(), pagination.rowCount());
        }
        return this;
    }

    public BaseDaoSQLBuilder Set(BaseDaoColumnInfo[] updateSetColumnInfos) {
        this.sqlBuilder.Set(Arrays.stream(updateSetColumnInfos).map(BaseDaoColumnInfo::updateSetSQL).toArray(String[]::new));
        return this;
    }

}
