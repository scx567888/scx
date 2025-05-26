package cool.scx.data.build_control;

import java.util.Collection;

public record BuildControlInfo(boolean skipIfNull,
                               boolean skipIfEmptyList,
                               boolean skipIfEmptyString,
                               boolean skipIfBlankString,
                               boolean useExpression,
                               boolean useExpressionValue) {

    public static BuildControlInfo ofInfo(BuildControl... controls) {
        var skipIfNull = false;
        var skipIfEmptyList = false;
        var skipIfEmptyString = false;
        var skipIfBlankString = false;
        var useExpression = false;
        var useExpressionValue = false;
        for (var c : controls) {
            switch (c) {
                case SKIP_IF_NULL -> skipIfNull = true;
                case SKIP_IF_EMPTY_LIST -> skipIfEmptyList = true;
                case SKIP_IF_EMPTY_STRING -> skipIfEmptyString = true;
                case SKIP_IF_BLANK_STRING -> skipIfBlankString = true;
                case USE_EXPRESSION -> useExpression = true;
                case USE_EXPRESSION_VALUE -> useExpressionValue = true;
            }
        }
        return new BuildControlInfo(skipIfNull, skipIfEmptyList, skipIfEmptyString, skipIfBlankString, useExpression, useExpressionValue);
    }

    public boolean shouldSkip(Object value1) {
        if (value1 == null) {
            return skipIfNull;
        }

        if (value1 instanceof Collection<?> collection) {
            return skipIfEmptyList && collection.isEmpty();
        }

        if (value1 instanceof Object[] array) {
            return skipIfEmptyList && array.length == 0;
        }

        if (value1 instanceof String s) {
            if (skipIfEmptyString && s.isEmpty()) {
                return true;
            }
            return skipIfBlankString && s.trim().isEmpty();
        }

        return false;
    }

    public boolean shouldSkip(Object value1, Object value2) {
        return shouldSkip(value1) || shouldSkip(value2);
    }

}
