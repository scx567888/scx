package cool.scx.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.ScxContext;
import cool.scx.bo.FileUpload;
import cool.scx.enumeration.ScxFeature;
import cool.scx.exception.BadRequestException;
import cool.scx.util.ObjectUtils;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>ScxMappingRequestParamInfo class.</p>
 *
 * @author scx567888
 * @version 1.4.7
 */
final class ScxMappingRequestParamInfo {

    private final RoutingContext routingContext;
    private final JsonNode jsonBody;
    private final Map<String, Object> formAttributesMap;
    private final Map<String, Object> queryParams;
    private final Map<String, String> pathParams;
    private final Map<String, FileUpload> fileUploadMap;
    private final boolean isJsonBody;//是不是 json格式的请求

    /**
     * <p>Constructor for ScxMappingRequestParamInfo.</p>
     *
     * @param ctx a {@link io.vertx.ext.web.RoutingContext} object
     */
    ScxMappingRequestParamInfo(RoutingContext ctx) {
        this.routingContext = ctx;
        this.jsonBody = initJsonBody(ctx);
        this.formAttributesMap = multiMapToMap(ctx.request().formAttributes());
        this.isJsonBody = this.formAttributesMap.size() == 0;
        this.queryParams = multiMapToMap(ctx.queryParams());
        this.pathParams = ctx.pathParams();
        this.fileUploadMap = ctx.get("uploadFiles") != null ? ctx.get("uploadFiles") : new HashMap<>();
    }

    private static JsonNode initJsonBody(RoutingContext ctx) {
        //先从多个来源获取参数 并缓存起来
        try {
            return ObjectUtils.readTree(ctx.getBodyAsString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * <p>multiMapToMap.</p>
     *
     * @param multiMap a {@link io.vertx.core.MultiMap} object
     * @return a {@link java.util.Map} object
     */
    public static Map<String, Object> multiMapToMap(MultiMap multiMap) {
        var map = new HashMap<String, Object>();
        for (var m : multiMap) {
            map.put(m.getKey(), m.getValue());
        }
        return map;
    }

    private static Object getWithException(Object v, boolean required, Type type) throws RequiredParamEmptyException, ParamConvertException {
        //为空的时候做两个处理 即必填则报错 非必填则返回 null
        if (required && v == null) {
            throw new RequiredParamEmptyException();
        }
        try {
            return ObjectUtils.convertValue(v, type);
        } catch (Exception e) {
            //和上方类似 针对是否是必填项进行不同的处理
            if (required) {
                throw new ParamConvertException(e);
            }
        }
        return null;
    }

    /**
     * <p>getFileUpload.</p>
     *
     * @param name a {@link java.lang.String} object
     * @return a {@link cool.scx.bo.FileUpload} object
     */
    public FileUpload getFileUpload(String name) {
        return fileUploadMap.get(name);
    }

    /**
     * <p>getBody.</p>
     *
     * @param v        a {@link java.lang.String} object
     * @param required a boolean
     * @param type     a {@link java.lang.reflect.Type} object
     * @return a {@link java.lang.Object} object
     * @throws BadRequestException if any.
     */
    public Object getBody(String v, boolean useAllBody, boolean required, Type type) throws RequiredParamEmptyException, ParamConvertException, BadRequestException {
        return isJsonBody ? getBodyFromJson(v, useAllBody, required, type) : getBodyFromFormAttributes(v, useAllBody, required, type);
    }

    /**
     * <p>getBodyFromJson.</p>
     *
     * @param bodyParamValue a {@link java.lang.String} object
     * @param required       a boolean
     * @param type           a {@link java.lang.reflect.Type} object
     * @return a {@link java.lang.Object} object
     */
    public Object getBodyFromJson(String bodyParamValue, boolean useAllBody, boolean required, Type type) throws RequiredParamEmptyException, ParamConvertException {
        var j = jsonBody;
        //根据 keyPath 拆解 jsonBody
        if (!useAllBody) {
            var split = bodyParamValue.split("\\.");
            for (String s : split) {
                if (j != null) {
                    j = j.get(s);
                } else {
                    break;
                }
            }
        }
        //为空的时候做两个处理 即必填则报错 非必填则返回 null
        if (required && (j == null || j.isNull())) {
            throw new RequiredParamEmptyException();
        }
        try {
            return ObjectUtils.readValue(j, type);
        } catch (Exception e) {
            //和上方类似 针对是否是必填项进行不同的处理
            if (required) {
                throw new ParamConvertException(e);
            }
        }
        return null;
    }

    /**
     * <p>getBodyFromFormAttributes.</p>
     *
     * @param bodyParamValue a {@link java.lang.String} object
     * @param required       a boolean
     * @param type           a {@link java.lang.reflect.Type} object
     * @return a {@link java.lang.Object} object
     */
    public Object getBodyFromFormAttributes(String bodyParamValue, boolean useAllBody, boolean required, Type type) throws ParamConvertException, RequiredParamEmptyException {
        return getWithException(useAllBody ? formAttributesMap : formAttributesMap.get(bodyParamValue), required, type);
    }

    /**
     * <p>getQuery.</p>
     *
     * @param value    a {@link java.lang.String} object
     * @param merge    a boolean
     * @param required a boolean
     * @param type     a {@link java.lang.reflect.Type} object
     * @return a {@link java.lang.Object} object
     */
    public Object getQuery(String value, boolean merge, boolean required, Type type) throws RequiredParamEmptyException, ParamConvertException {
        return getWithException(merge ? queryParams : queryParams.get(value), required, type);
    }

    /**
     * <p>getPath.</p>
     *
     * @param value    a {@link java.lang.String} object
     * @param merge    a boolean
     * @param required a boolean
     * @param type     a {@link java.lang.reflect.Type} object
     * @return a {@link java.lang.Object} object
     */
    public Object getPath(String value, boolean merge, boolean required, Type type) throws RequiredParamEmptyException, ParamConvertException {
        return getWithException(merge ? pathParams : pathParams.get(value), required, type);
    }

    Object[] convert(ScxMappingHandlerMethodParamInfo[] paramsInfos) throws BadRequestException {
        //存储转换过程中可能出现的错误信息
        var errMessageMap = new ArrayList<String>();
        var finalHandlerParams = new Object[paramsInfos.length];
        for (int i = 0; i < finalHandlerParams.length; i++) {
            var p = paramsInfos[i];
            try {
                if (p.javaType() == RoutingContext.class) {
                    finalHandlerParams[i] = routingContext;
                } else if (p.javaType() == FileUpload.class) {
                    finalHandlerParams[i] = getFileUpload(p.name());
                } else if (p.fromType() == ScxMappingFromType.FROM_BODY) {
                    finalHandlerParams[i] = getBody(p.name(), p.isUseAllBody(), p.isRequired(), p.javaType());
                } else if (p.fromType() == ScxMappingFromType.FROM_QUERY) {
                    finalHandlerParams[i] = getQuery(p.name(), p.isMerge(), p.isRequired(), p.javaType());
                } else if (p.fromType() == ScxMappingFromType.FROM_PATH) {
                    finalHandlerParams[i] = getPath(p.name(), p.isMerge(), p.isRequired(), p.javaType());
                } else {
                    //------ 这里针对没有注解的参数进行赋值猜测 ---------------
                    //  从 body 里进行猜测 先尝试 根据参数名称进行转换
                    finalHandlerParams[i] = getBody(p.name(), false, false, p.javaType());
                    if (finalHandlerParams[i] == null) {
                        // 再尝试将整体转换为 参数
                        finalHandlerParams[i] = getBody(null, true, false, p.javaType());
                        if (finalHandlerParams[i] == null) {
                            //从查询参数里进行猜测
                            finalHandlerParams[i] = getQuery(p.name(), false, false, p.javaType());
                            if (finalHandlerParams[i] == null) {
                                //从路径进行猜测
                                finalHandlerParams[i] = getPath(p.name(), false, false, p.javaType());
                            }
                        }
                    }
                }
            } catch (ParamConvertException e) {
                errMessageMap.add("参数类型转换异常 !!! 参数名称 [" + p.name() + "] , 参数来源 [" + p.fromType() + ", useAllBody=" + p.isUseAllBody() + ", merge=" + p.isMerge() + "] , 参数类型 [" + p.javaType().getTypeName() + "] , 详细错误信息 : " + e.exception.getMessage());
            } catch (RequiredParamEmptyException e) {
                errMessageMap.add("必填参数不能为空 !!! 参数名称 [" + p.name() + "] , 参数来源 [" + p.fromType() + ", useAllBody=" + p.isUseAllBody() + ", merge=" + p.isMerge() + "] , 参数类型 [" + p.javaType().getTypeName() + "]");
            }
        }
        if (!errMessageMap.isEmpty()) {
            //是否使用开发时错误页面
            if (ScxContext.getFeatureState(ScxFeature.USE_DEVELOPMENT_ERROR_PAGE)) {
                throw new BadRequestException(errMessageMap);
            } else {
                throw new BadRequestException();
            }
        }
        return finalHandlerParams;
    }

    private static class RequiredParamEmptyException extends Exception {

    }

    private static class ParamConvertException extends Exception {
        public final Exception exception;

        public ParamConvertException(Exception e) {
            this.exception = e;
        }
    }

}