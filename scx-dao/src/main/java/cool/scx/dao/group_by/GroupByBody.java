package cool.scx.dao.group_by;

import cool.scx.sql.TableInfo;
import cool.scx.util.StringUtils;

import static cool.scx.dao.ColumnNameParser.parseColumnName;

/**
 * <p>GroupByBody class.</p>
 *
 * @author scx567888
 * @version 0.0.1
 */
record GroupByBody(String name, GroupByOption.Info info) {

    GroupByBody(String name, GroupByOption.Info info) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("GroupBy 参数错误 : 名称 不能为空 !!!");
        }
        this.name = name.trim();
        this.info = info;
    }

    String groupByColumn(TableInfo<?> tableInfo) {
        return parseColumnName(tableInfo, this.name, info.useJsonExtract(), info.useOriginalName());
    }

}
