package cool.scx.data.jdbc;

import cool.scx.data.jdbc.annotation.NoColumn;
import cool.scx.data.jdbc.mapping.Table;
import cool.scx.util.CaseUtils;
import cool.scx.util.MultiMap;
import cool.scx.util.StringUtils;
import cool.scx.util.reflect.FieldUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class AnnotationConfigTable implements Table<AnnotationConfigColumn> {

    /**
     * 实体类型不含 @NoColumn 注解的field
     */
    private final AnnotationConfigColumn[] columns;

    /**
     * 表名
     */
    private final String name;

    /**
     * 因为 循环查找速度太慢了 所以这里 使用 map (key:javaFieldName,value:ColumnInfo)
     */
    private final Map<String, AnnotationConfigColumn> columnMap;

    public AnnotationConfigTable(Class<?> clazz) {
        this.name = initTableName(clazz);
        this.columns = initAllColumns(clazz);
        this.columnMap = initAllColumnMap(columns);
    }

    public static String initTableName(Class<?> clazz) {
        var scxModel = clazz.getAnnotation(cool.scx.data.jdbc.annotation.Table.class);
        if (scxModel != null && StringUtils.notBlank(scxModel.tableName())) {
            return scxModel.tableName();
        }
        if (scxModel != null && StringUtils.notBlank(scxModel.tablePrefix())) {
            return scxModel.tablePrefix() + "_" + CaseUtils.toSnake(clazz.getSimpleName());
        }
        //这里判断一下是否使用了数据库 如果使用 则表名省略掉 数据库限定名 否则的话则添加数据库限定名
        return "scx_" + CaseUtils.toSnake(clazz.getSimpleName());
    }

    private static AnnotationConfigColumn[] initAllColumns(Class<?> clazz) {
        var list = Stream.of(FieldUtils.findFields(clazz))
                .filter(field -> !field.isAnnotationPresent(NoColumn.class))
                .map(AnnotationConfigColumn::new)
                .toList();
        checkDuplicateColumnName(list, clazz);
        return list.toArray(AnnotationConfigColumn[]::new);
    }

    private static Map<String, AnnotationConfigColumn> initAllColumnMap(AnnotationConfigColumn[] infos) {
        var map = new HashMap<String, AnnotationConfigColumn>();
        for (var info : infos) {
            map.put(info.name(), info);
        }
        // javaFieldName 的优先级大于 columnName 所以允许覆盖
        for (var info : infos) {
            map.put(info.javaField().getName(), info);
        }
        return map;
    }

    /**
     * 检测 columnName 重复值
     *
     * @param list  a
     * @param clazz a
     */
    private static void checkDuplicateColumnName(List<AnnotationConfigColumn> list, Class<?> clazz) {
        var multiMap = new MultiMap<String, AnnotationConfigColumn>();
        for (var info : list) {
            multiMap.put(info.name(), info);
        }
        var map = multiMap.toMultiValueMap();
        for (var entry : map.entrySet()) {
            var v = entry.getValue();
            if (v.size() > 1) { //具有多个相同的 columnName 值
                throw new IllegalArgumentException("重复的 columnName !!! Class -> " + clazz.getName() + ", Field -> " + v.stream().map(c -> c.javaField().getName()).toList());
            }
        }
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public AnnotationConfigColumn[] columns() {
        return this.columns;
    }

    @Override
    public AnnotationConfigColumn getColumn(String column) {
        return this.columnMap.get(column);
    }

}
