package cool.scx.dao;

import cool.scx.dao.annotation.NoColumn;
import cool.scx.dao.annotation.Table;
import cool.scx.sql.TableInfo;
import cool.scx.util.CaseUtils;
import cool.scx.util.StringUtils;

import java.util.stream.Stream;

/**
 * 通过注解配置的 TableInfo
 *
 * @author scx567888
 * @version 0.5.0
 */
public final class AnnotationConfigTableInfo implements TableInfo<AnnotationConfigColumnInfo> {

    /**
     * 实体类型不含 @NoColumn 注解的field
     */
    private final AnnotationConfigColumnInfo[] columnInfos;

    /**
     * 表名
     */
    private final String tableName;

    /**
     * c
     *
     * @param clazz a {@link java.lang.Class} object.
     */
    public AnnotationConfigTableInfo(Class<?> clazz) {
        this.tableName = initTableName(clazz);
        this.columnInfos = initAllColumnInfos(clazz);
    }

    /**
     * a
     *
     * @param clazz a
     * @return a
     */
    private static AnnotationConfigColumnInfo[] initAllColumnInfos(Class<?> clazz) {
        return Stream.of(clazz.getFields()).filter(field -> !field.isAnnotationPresent(NoColumn.class)).map(AnnotationConfigColumnInfo::new).toArray(AnnotationConfigColumnInfo[]::new);
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

    /**
     * {@inheritDoc}
     * <p>
     * a
     */
    @Override
    public AnnotationConfigColumnInfo[] columnInfos() {
        return columnInfos;
    }

}
