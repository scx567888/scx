package cool.scx.data.build_control;

public record BuildControlInfo(boolean replace,
                               boolean skipIfNull,
                               boolean skipIfEmptyList,
                               boolean useExpression,
                               boolean useJsonExtract,
                               boolean useOriginalValue) {

    public static BuildControlInfo ofInfo(BuildControl... controls) {
        var replace = false;
        var skipIfNull = false;
        var skipIfEmptyList = false;
        var useExpression = false;
        var useJsonExtract = false;
        var useOriginalValue = false;
        for (var c : controls) {
            switch (c) {
                case REPLACE -> replace = true;
                case SKIP_IF_NULL -> skipIfNull = true;
                case SKIP_IF_EMPTY_LIST -> skipIfEmptyList = true;
                case USE_EXPRESSION -> useExpression = true;
                case USE_JSON_EXTRACT -> useJsonExtract = true;
                case USE_ORIGINAL_VALUE -> useOriginalValue = true;
            }
        }
        return new BuildControlInfo(replace, skipIfNull, skipIfEmptyList, useExpression, useJsonExtract, useOriginalValue);
    }

}
