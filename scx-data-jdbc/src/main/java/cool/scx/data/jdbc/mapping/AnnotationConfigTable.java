package cool.scx.data.jdbc.mapping;

import cool.scx.collections.multi_map.MultiMap;
import cool.scx.common.util.CaseUtils;
import cool.scx.data.jdbc.annotation.NoColumn;
import cool.scx.reflect.ClassInfoFactory;
import cool.scx.reflect.FieldInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static cool.scx.common.constant.AnnotationValueHelper.getRealValue;
import static cool.scx.reflect.AccessModifier.PUBLIC;
import static cool.scx.reflect.ClassType.RECORD;

/// AnnotationConfigTable
///
/// @author scx567888
/// @version 0.0.1
public class AnnotationConfigTable<Entity> implements EntityTable<Entity> {

    /// 实体类型不含 @NoColumn 注解的field
    private final AnnotationConfigColumn[] columns;

    /// 表名
    private final String name;

    /// 因为 循环查找速度太慢了 所以这里 使用 map (key:javaFieldName,value:ColumnInfo)
    private final Map<String, AnnotationConfigColumn> columnMap;

    private final Class<Entity> entityClass;

    public AnnotationConfigTable(Class<Entity> entityClass) {
        this.entityClass = entityClass;
        this.name = initTableName(entityClass);
        this.columns = initAllColumns(entityClass);
        this.columnMap = initAllColumnMap(columns);
    }

    /// 这里判断一下是否使用了数据库 如果使用 则表名省略掉 数据库限定名 否则的话则添加数据库限定名
    ///
    /// @param clazz c
    /// @return c
    public static String initTableName(Class<?> clazz) {
        var table = clazz.getAnnotation(cool.scx.data.jdbc.annotation.Table.class);
        var defaultTableName = CaseUtils.toSnake(clazz.getSimpleName());

        if (table != null) {
            var _tableName = getRealValue(table.value());

            if (_tableName != null) {
                return _tableName;
            }
        }

        return defaultTableName;
    }

    private static AnnotationConfigColumn[] initAllColumns(Class<?> clazz) {
        var classInfo = ClassInfoFactory.getClassInfo(clazz);
        var fields = classInfo.classType() == RECORD ? classInfo.allFields() : Stream.of(classInfo.allFields()).filter(c -> c.accessModifier() == PUBLIC).toArray(FieldInfo[]::new);
        var list = Stream.of(fields)
                .filter(field -> field.findAnnotation(NoColumn.class) == null)
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
            map.put(info.javaField().name(), info);
        }
        return map;
    }

    /// 检测 columnName 重复值
    ///
    /// @param list  a
    /// @param clazz a
    private static void checkDuplicateColumnName(List<AnnotationConfigColumn> list, Class<?> clazz) {
        var multiMap = new MultiMap<String, AnnotationConfigColumn>();
        for (var info : list) {
            multiMap.add(info.name(), info);
        }
        for (var entry : multiMap) {
            var v = entry.values();
            if (v.size() > 1) { //具有多个相同的 columnName 值
                throw new IllegalArgumentException("重复的 columnName !!! Class -> " + clazz.getName() + ", Field -> " + v.stream().map(c -> c.javaField().name()).toList());
            }
        }
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Class<Entity> entityClass() {
        return entityClass;
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
