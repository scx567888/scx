package cool.scx.data.aggregation;

import cool.scx.data.query.WhereType;

public enum GroupByOption {

    /// 使用原始名称 (不进行转换)
    USE_ORIGINAL_NAME,

    /// 使用 JSON 查询
    /// 注意和 {@link WhereType#JSON_CONTAINS} 一起使用时无效 因为 {@link WhereType#JSON_CONTAINS} 自己有针对 Json 的特殊实现
    USE_JSON_EXTRACT;

    public static GroupByOption.Info ofInfo(GroupByOption... groupByOptions) {
        var useOriginalName = false;
        var useJsonExtract = false;
        for (var option : groupByOptions) {
            switch (option) {
                case USE_ORIGINAL_NAME -> useOriginalName = true;
                case USE_JSON_EXTRACT -> useJsonExtract = true;
            }
        }
        return new GroupByOption.Info(useOriginalName, useJsonExtract);
    }

    public record Info(boolean useOriginalName, boolean useJsonExtract) {

    }

}
