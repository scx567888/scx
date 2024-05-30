package cool.scx.common.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;

import java.util.Map;

public record BuildOptions(
        boolean ignoreNullValue,
        boolean ignoreJsonIgnore,
        boolean failOnUnknownProperties,
        boolean failOnEmptyBeans,
        Map<PropertyAccessor, JsonAutoDetect.Visibility> visibilityConfig
) {

}
