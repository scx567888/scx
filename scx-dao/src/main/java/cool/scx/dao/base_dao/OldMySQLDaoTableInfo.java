package cool.scx.dao.base_dao;

import cool.scx.dao.annotation.NoColumn;
import cool.scx.dao.annotation.Table;
import cool.scx.sql.ColumnInfo;
import cool.scx.sql.TableInfo;
import cool.scx.util.CaseUtils;
import cool.scx.util.MultiMap;
import cool.scx.util.StringUtils;
import cool.scx.util.reflect.FieldUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Deprecated
public class OldMySQLDaoTableInfo implements TableInfo<OldMySQLDaoColumnInfo> {

    /**
     * 实体类型不含 @NoColumn 注解的field
     */
    private final OldMySQLDaoColumnInfo[] columnInfos;

    /**
     * 表名
     */
    private final String tableName;

    /**
     * 因为 循环查找速度太慢了 所以这里 使用 map (key:javaFieldName,value:ColumnInfo)
     */
    private final Map<String, OldMySQLDaoColumnInfo> columnInfoMap;

    /**
     * c
     *
     * @param clazz a {@link java.lang.Class} object.
     */
    public OldMySQLDaoTableInfo(Class<?> clazz) {
        this.tableName = initTableName(clazz);
        this.columnInfos = initAllColumnInfos(clazz);
        this.columnInfoMap = initAllColumnInfoMap(columnInfos);
    }

    /**
     * a
     *
     * @param clazz a
     * @return a
     */
    private static OldMySQLDaoColumnInfo[] initAllColumnInfos(Class<?> clazz) {
        var list = Stream.of(FieldUtils.findFields(clazz))
                .filter(field -> !field.isAnnotationPresent(NoColumn.class))
                .map(OldMySQLDaoColumnInfo::new)
                .toList();
        checkDuplicateColumnName(list, clazz);
        return list.toArray(OldMySQLDaoColumnInfo[]::new);
    }

    private static Map<String, OldMySQLDaoColumnInfo> initAllColumnInfoMap(OldMySQLDaoColumnInfo[] infos) {
        var map = new HashMap<String, OldMySQLDaoColumnInfo>();
        for (var info : infos) {
            map.put(info.columnName(), info);
        }
        // javaFieldName 的优先级大于 columnName 所以允许覆盖
        for (var info : infos) {
            map.put(info.javaFieldName(), info);
        }
        return map;
    }

    /**
     * 检测 columnName 重复值
     *
     * @param list  a
     * @param clazz a
     */
    private static void checkDuplicateColumnName(List<OldMySQLDaoColumnInfo> list, Class<?> clazz) {
        var multiMap = new MultiMap<String, OldMySQLDaoColumnInfo>();
        for (var info : list) {
            multiMap.put(info.columnName(), info);
        }
        var map = multiMap.toMultiValueMap();
        for (var entry : map.entrySet()) {
            var v = entry.getValue();
            if (v.size() > 1) { //具有多个相同的 columnName 值
                throw new IllegalArgumentException("重复的 columnName !!! Class -> " + clazz.getName() + ", Field -> " + v.stream().map(ColumnInfo::javaFieldName).toList());
            }
        }
    }

    /**
     * a
     *
     * @param clazz a
     * @return a
     */
    private static String initTableName(Class<?> clazz) {
        var scxModel = clazz.getAnnotation(Table.class);
        if (scxModel != null && StringUtils.notBlank(scxModel.tableName())) {
            return scxModel.tableName();
        }
        if (scxModel != null && StringUtils.notBlank(scxModel.tablePrefix())) {
            return scxModel.tablePrefix() + "_" + CaseUtils.toSnake(clazz.getSimpleName());
        }
        //这里判断一下是否使用了数据库 如果使用 则表名省略掉 数据库限定名 否则的话则添加数据库限定名
        return "scx_" + CaseUtils.toSnake(clazz.getSimpleName());
    }

    /**
     * {@inheritDoc}
     * <p>
     * a
     */
    @Override
    public String tableName() {
        return tableName;
    }

    @Override
    public OldMySQLDaoColumnInfo[] columnInfos() {
        return columnInfos;
    }

    @Override
    public OldMySQLDaoColumnInfo getColumnInfo(String javaFieldName) {
        return columnInfoMap.get(javaFieldName);
    }

}
