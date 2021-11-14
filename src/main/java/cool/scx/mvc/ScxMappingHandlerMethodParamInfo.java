package cool.scx.mvc;

import cool.scx.annotation.FromBody;
import cool.scx.annotation.FromPath;
import cool.scx.annotation.FromQuery;
import cool.scx.util.StringUtils;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

/**
 * 格式化后的 参数信息
 *
 * @author scx567888
 * @version 1.3.14
 */
public final class ScxMappingHandlerMethodParamInfo {

    /**
     * 类型
     */
    private final Type javaType;
    /**
     * 原始 Parameter
     */
    private final Parameter javaParameter;
    /**
     * 参数名称 默认取 参数名称 也可以由 参数上的注解覆盖
     */
    private String name;
    /**
     * 使用全部 body
     */
    private boolean useAllBody;
    /**
     * 是否合并参数
     */
    private boolean merge;
    /**
     * 数据来源
     */
    private ScxMappingFromType fromType;
    /**
     * 是否不为空
     */
    private boolean required;

    /**
     * <p>Constructor for ScxMappingParam.</p>
     *
     * @param javaParameter a {@link java.lang.reflect.Parameter} object
     */
    public ScxMappingHandlerMethodParamInfo(Parameter javaParameter) {
        this.javaParameter = javaParameter;
        this.javaType = javaParameter.getParameterizedType();
        this.fromType = ScxMappingFromType.UNKNOWN;
        this.required = false;
        this.name = javaParameter.getName();
        this.merge = false;
        this.useAllBody = false;

        for (var a : javaParameter.getAnnotations()) {
            var aClass = a.annotationType();
            if (aClass == FromBody.class) {
                this.fromType = ScxMappingFromType.FROM_BODY;
                var fromBody = (FromBody) a;
                this.required = fromBody.required();
                if (StringUtils.isNotBlank(fromBody.value())) {
                    this.name = fromBody.value();
                }
                this.useAllBody = fromBody.useAllBody();
            } else if (aClass == FromQuery.class) {
                this.fromType = ScxMappingFromType.FROM_QUERY;
                var fromQuery = (FromQuery) a;
                this.required = fromQuery.required();
                if (StringUtils.isNotBlank(fromQuery.value())) {
                    this.name = fromQuery.value();
                }
                this.merge = fromQuery.merge();
            } else if (aClass == FromPath.class) {
                var fromPath = (FromPath) a;
                this.fromType = ScxMappingFromType.FROM_PATH;
                this.required = fromPath.required();
                if (StringUtils.isNotBlank(fromPath.value())) {
                    this.name = fromPath.value();
                }
                this.merge = fromPath.merge();
            }
        }
    }

    public String name() {
        return name;
    }

    public boolean isUseAllBody() {
        return useAllBody;
    }

    public boolean isMerge() {
        return merge;
    }

    public ScxMappingFromType fromType() {
        return fromType;
    }

    public boolean isRequired() {
        return required;
    }

    public Type javaType() {
        return javaType;
    }

    public Parameter javaParameter() {
        return javaParameter;
    }

}