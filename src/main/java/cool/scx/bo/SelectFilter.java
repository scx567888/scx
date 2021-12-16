package cool.scx.bo;

import cool.scx.dao.ScxDaoColumnInfo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 查询列过滤器
 */
public final class SelectFilter {

    private final Set<String> includedFieldNames = new HashSet<>();
    private final Set<String> excludedFieldNames = new HashSet<>();
    /**
     * 过滤器类型 分三种 禁用 : 0 ,包含模式 : 1 排除模式 : 2
     */
    private int mode = 0;

    /**
     * 添加 包含类型的列
     *
     * @param includedFieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
     * @return this 方便链式调用
     */
    public SelectFilter addIncluded(String... includedFieldNames) {
        this.includedFieldNames.addAll(Arrays.asList(includedFieldNames));
        return this;
    }

    /**
     * 添加 排除类型的列
     *
     * @param excludedFieldNames 排除的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
     * @return this 方便链式调用
     */
    public SelectFilter addExcluded(String... excludedFieldNames) {
        this.excludedFieldNames.addAll(Arrays.asList(excludedFieldNames));
        return this;
    }

    /**
     * 根据指定名称 移除 包含类型的列
     *
     * @param includedFieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
     * @return this 方便链式调用
     */
    public SelectFilter removeIncluded(String... includedFieldNames) {
        for (String includedFieldName : includedFieldNames) {
            this.includedFieldNames.remove(includedFieldName);
        }
        return this;
    }

    /**
     * 根据指定名称 移除 排除类型的列
     *
     * @param excludedFieldNames 排除的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
     * @return this 方便链式调用
     */
    public SelectFilter removeExcluded(String... excludedFieldNames) {
        for (String includedFieldName : excludedFieldNames) {
            this.excludedFieldNames.remove(includedFieldName);
        }
        return this;
    }

    /**
     * 清除所有 包含类型的列
     *
     * @return this 方便链式调用
     */
    public SelectFilter clearIncluded() {
        this.includedFieldNames.clear();
        return this;
    }

    /**
     * 清除所有 排除类型的列
     *
     * @return this 方便链式调用
     */
    public SelectFilter clearExcluded() {
        this.excludedFieldNames.clear();
        return this;
    }

    /**
     * 关闭列 filter
     *
     * @return this 方便链式调用
     */
    public SelectFilter disableFilter() {
        this.mode = 0;
        return this;
    }

    /**
     * 使用包含模式
     *
     * @return this 方便链式调用
     */
    public SelectFilter useIncludeMode() {
        this.mode = 1;
        return this;
    }


    /**
     * 使用排除模式
     *
     * @return this 方便链式调用
     */
    public SelectFilter useExcludeMode() {
        this.mode = 2;
        return this;
    }

    /**
     * 过滤
     *
     * @param scxDaoColumnInfoList 带过滤的列表
     * @return 过滤后的列表
     */
    public List<ScxDaoColumnInfo> filter(List<ScxDaoColumnInfo> scxDaoColumnInfoList) {
        if (this.mode == 0) {
            return scxDaoColumnInfoList;
        } else if (this.mode == 1) {
            return scxDaoColumnInfoList.stream().filter(c -> this.includedFieldNames.contains(c.fieldName())).collect(Collectors.toList());
        } else if (this.mode == 2) {
            return scxDaoColumnInfoList.stream().filter(c -> !this.excludedFieldNames.contains(c.fieldName())).collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("未知 mode 类型");
        }
    }

    /**
     * 过滤
     *
     * @param scxDaoColumnInfos 带过滤的列表
     * @return 过滤后的列表
     */
    public ScxDaoColumnInfo[] filter(ScxDaoColumnInfo... scxDaoColumnInfos) {
        if (this.mode == 0) {
            return scxDaoColumnInfos;
        } else if (this.mode == 1) {
            return Arrays.stream(scxDaoColumnInfos).filter(c -> this.includedFieldNames.contains(c.fieldName())).toArray(ScxDaoColumnInfo[]::new);
        } else if (this.mode == 2) {
            return Arrays.stream(scxDaoColumnInfos).filter(c -> !this.excludedFieldNames.contains(c.fieldName())).toArray(ScxDaoColumnInfo[]::new);
        } else {
            throw new IllegalArgumentException("未知 mode 类型");
        }
    }

    /**
     * 获取当前模式
     *
     * @return mode 分三种 禁用 : 0 ,包含模式 : 1 排除模式 : 2
     */
    public int mode() {
        return mode;
    }

}
