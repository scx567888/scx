package cool.scx.core.dao;

import cool.scx.core.annotation.NoColumn;
import cool.scx.core.annotation.ScxModel;
import cool.scx.sql.TableInfo;
import cool.scx.util.CaseUtils;
import cool.scx.util.StringUtils;

import java.util.stream.Stream;

/**
 * 用于描述根据 class 构建的对应的数据表结构
 *
 * @author scx567888
 * @version 0.5.0
 */
public final class ScxDaoTableInfo implements TableInfo<ScxDaoColumnInfo> {

    /**
     * 实体类型不含@NoColunm 注解的field
     */
    private final ScxDaoColumnInfo[] columnInfos;

    /**
     * 表名
     */
    private final String tableName;

    /**
     * c
     *
     * @param clazz a {@link java.lang.Class} object.
     */
    public ScxDaoTableInfo(Class<?> clazz) {
        this.tableName = initTableName(clazz);
        this.columnInfos = initAllColumnInfos(clazz);
    }

    /**
     * a
     *
     * @param clazz a
     * @return a
     */
    private static ScxDaoColumnInfo[] initAllColumnInfos(Class<?> clazz) {
        return Stream.of(clazz.getFields()).filter(field -> !field.isAnnotationPresent(NoColumn.class)).map(ScxDaoColumnInfo::new).toArray(ScxDaoColumnInfo[]::new);
    }

    /**
     * a
     *
     * @param clazz a
     * @return a
     */
    private static String initTableName(Class<?> clazz) {
        var scxModel = clazz.getAnnotation(ScxModel.class);
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
    public ScxDaoColumnInfo[] columnInfos() {
        return columnInfos;
    }

}
