package cool.scx.dao.order_by;

import cool.scx.sql.TableInfo;
import cool.scx.util.StringUtils;

import static cool.scx.dao.ColumnNameParser.parseColumnName;

/**
 * OrderBy 封装体
 *
 * @author scx567888
 * @version 0.0.1
 */
record OrderByBody(String name, OrderByType orderByType, OrderByOption.Info info) {

    OrderByBody(String name, OrderByType orderByType, OrderByOption.Info info) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("OrderBy 参数错误 : 名称 不能为空 !!!");
        }
        if (orderByType == null) {
            throw new IllegalArgumentException("OrderBy 参数错误 : orderByType 不能为空 !!!");
        }
        this.name = name.trim();
        this.orderByType = orderByType;
        this.info = info;
    }

    String orderByClause(TableInfo<?> tableInfo) {
        var columnName = parseColumnName(tableInfo, this.name, info.useJsonExtract(), info.useOriginalName());
        return columnName + " " + orderByType.name();
    }

}
