package cool.scx.data.query;

/**
 * QueryOption
 *
 * @author scx567888
 * @version 0.0.1
 */
public enum QueryOption {

    /**
     * 替换现有
     */
    REPLACE,

    /**
     * 如果查询的参数值为 null 则跳过添加而不是报错
     * <br>
     * 这里虽然叫做 SKIP_IF_NULL 但实际上表示的有效参数数量是不是和所接受的参数数量一致
     * <br>
     * 只是为了简化书写
     */
    SKIP_IF_NULL,

    /**
     * 在 in 或 not in 中 如果有效的参数条目 (指去除 null 后的) 为空 则跳过添加而不是报错
     * <br>
     * 和 {@link  QueryOption#SKIP_IF_NULL} 相同 是为了简化书写 其实际意义为参数中去除非法数值(为 null)后的列表长度是否为 0
     */
    SKIP_IF_EMPTY_LIST,

    /**
     * 使用原始名称 (不进行转换)
     */
    USE_ORIGINAL_NAME,

    /**
     * 使用 JSON 查询
     * <br>
     * 注意和 {@link WhereType#JSON_CONTAINS} 一起使用时无效 因为 {@link WhereType#JSON_CONTAINS} 自己有针对 Json 的特殊实现
     */
    USE_JSON_EXTRACT,

    /**
     * 注意只适用于 JSON_CONTAINS
     * JSON_CONTAINS 默认会将值转换为 JSON 并去除为 value 为 null 的 字段
     * 使用 原始值 时会将值 直接传递到 SQL 语句
     * 若值为 实体类 则会转换为 JSON 不过 和默认情况相比, 转换的 JSON 会包含 value 为 null 的字段
     */
    USE_ORIGINAL_VALUE;

    public static Info ofInfo(QueryOption... queryOptions) {
        var replace = false;
        var skipIfNull = false;
        var skipIfEmptyList = false;
        var useOriginalName = false;
        var useJsonExtract = false;
        var useOriginalValue = false;
        for (var option : queryOptions) {
            switch (option) {
                case REPLACE -> replace = true;
                case SKIP_IF_NULL -> skipIfNull = true;
                case SKIP_IF_EMPTY_LIST -> skipIfEmptyList = true;
                case USE_ORIGINAL_NAME -> useOriginalName = true;
                case USE_JSON_EXTRACT -> useJsonExtract = true;
                case USE_ORIGINAL_VALUE -> useOriginalValue = true;
            }
        }
        return new Info(replace, skipIfNull, skipIfEmptyList, useOriginalName, useJsonExtract, useOriginalValue);
    }

    public record Info(boolean replace,
                       boolean skipIfNull,
                       boolean skipIfEmptyList,
                       boolean useOriginalName,
                       boolean useJsonExtract,
                       boolean useOriginalValue) {

    }

}
