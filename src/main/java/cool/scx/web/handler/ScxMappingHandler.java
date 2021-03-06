package cool.scx.web.handler;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.annotation.FromBody;
import cool.scx.annotation.FromPath;
import cool.scx.annotation.FromQuery;
import cool.scx.annotation.ScxMapping;
import cool.scx.auth.AuthUser;
import cool.scx.auth.ScxAuth;
import cool.scx.bo.FileUpload;
import cool.scx.context.ScxContext;
import cool.scx.enumeration.DeviceType;
import cool.scx.exception.BadRequestException;
import cool.scx.exception.HttpRequestException;
import cool.scx.util.CaseUtils;
import cool.scx.util.ObjectUtils;
import cool.scx.util.StringUtils;
import cool.scx.vo.BaseVo;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>ScxRouteHandler class.</p>
 *
 * @author scx567888
 * @version 0.3.6
 */
public class ScxMappingHandler implements Handler<RoutingContext> {

    public final Method method;
    public final ScxMapping methodScxMapping;
    public final ScxMapping classScxMapping;
    public final Object example;
    public final int order;
    public final Class<?> clazz;
    public final String url;
    public final Set<HttpMethod> httpMethods;
    public final String permStr;

    /**
     * <p>Constructor for ScxRouteHandler.</p>
     *
     * @param method a {@link java.lang.reflect.Method} object.
     * @param clazz  a {@link java.lang.Class} object.
     */
    public ScxMappingHandler(Class<?> clazz, Method method) {
        this.clazz = clazz;
        this.classScxMapping = clazz.getAnnotation(ScxMapping.class);
        this.method = method;
        this.methodScxMapping = method.getAnnotation(ScxMapping.class);
        this.example = ScxContext.getBean(clazz);
        this.url = getUrl();
        this.order = getOrder();
        this.httpMethods = getHttpMethod();
        this.permStr = clazz.getSimpleName() + ":" + method.getName();
    }

    private static Object getParamFromMap(Map<String, ?> map, String value, boolean merge, Parameter parameter, boolean required) throws BadRequestException {
        if (StringUtils.isEmpty(value)) {
            value = parameter.getName();
        }
        try {
            if (merge) {
                return ObjectUtils.mapToBean(map, parameter.getType());
            } else {
                return ObjectUtils.parseSimpleType(map.get(value), parameter.getType());
            }
        } catch (Exception e) {
            if (required) {
                throw new BadRequestException(e);
            } else {
                return null;
            }
        }
    }

    private static Object getParamFromBody(JsonNode jsonNode, Map<String, Object> formAttributesMap, String bodyParamValue, Parameter parameter, boolean required) throws BadRequestException {
        if (formAttributesMap.size() == 0) {
            var j = jsonNode;
            if (bodyParamValue != null) {
                if ("".equals(bodyParamValue)) {
                    bodyParamValue = parameter.getName();
                }
                var split = bodyParamValue.split("\\.");
                for (String s : split) {
                    if (j != null) {
                        j = j.get(s);
                    }
                }
            }
            try {
                return ObjectUtils.jsonNodeToBean(j, parameter.getParameterizedType());
            } catch (Exception e) {
                if (required) {
                    throw new BadRequestException(e);
                } else {
                    return null;
                }
            }
        } else {
            try {
                if (StringUtils.isEmpty(bodyParamValue)) {
                    return ObjectUtils.mapToBean(formAttributesMap, parameter.getType());
                } else {
                    return ObjectUtils.parseSimpleType(formAttributesMap.get(bodyParamValue), parameter.getType());
                }
            } catch (Exception e) {
                if (required) {
                    throw new BadRequestException(e);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * <p>multiMapToMap.</p>
     *
     * @param multiMap a {@link io.vertx.core.MultiMap} object.
     * @return a {@link java.util.Map} object.
     */
    public static Map<String, Object> multiMapToMap(MultiMap multiMap) {
        var map = new HashMap<String, Object>();
        for (var m : multiMap) {
            map.put(m.getKey(), m.getValue());
        }
        return map;
    }

    /**
     * ???????????????
     *
     * @param e ??????
     */
    private static void exceptionHandler0(Throwable e, RoutingContext context) {
        //???????????????????????????????????? ????????????????????? ??????????????????
        Throwable exception = (e instanceof InvocationTargetException) ? e.getCause() : e;
        //??????????????????????????????????????????
        if (exception instanceof HttpRequestException) {
            ((HttpRequestException) exception).exceptionHandler(context);
            return;
        }
        context.response().setStatusCode(500).end("Internal Server Error !!!");
        e.printStackTrace();
    }

    /**
     * ???????????????????????????
     *
     * @param context ???????????????
     * @return ???????????? true ??? ?????????????????????????????? false ????????????????????????
     */
    private boolean checkedLoginAndPerms(RoutingContext context) throws Exception {
        //?????? ??????????????? ?????????????????????????????? ?????? ?????????????????? true
        if (!methodScxMapping.checkedLogin()) {
            return true;
        } else {
            //?????????????????????
            AuthUser currentUser = ScxAuth.getLoginUser();
            //session ????????????????????????????????? ?????? false
            if (currentUser == null) {
                ScxAuth.authHandler().noLoginHandler(ScxAuth.getDevice(context), context);
                return false;
            } else {
                //???????????? ???????????? ?????? ????????????????????????????????????
                //????????? ???????????? ???????????? true
                if (!methodScxMapping.checkedPerms()) {
                    return true;
                } else {
                    //???????????? ???????????????  ??????????????????
                    if (currentUser._IsAdmin()) {
                        return true;
                    } else {
                        //????????????????????????????????????
                        var permStrByUser = ScxAuth.authHandler().getPerms(currentUser);
                        if (permStrByUser.contains(permStr)) {
                            return true;
                        } else {
                            ScxAuth.authHandler().noPermsHandler(ScxAuth.getDevice(context), context);
                            return false;
                        }
                    }
                }
            }
        }
    }

    private String getUrl() {
        var urlList = new ArrayList<String>();
        //??????????????? url
        if (classScxMapping.useNameAsUrl() && "".equals(classScxMapping.value())) {
            urlList.add(CaseUtils.toKebab(clazz.getSimpleName().replace("Controller", "")));
        } else {
            urlList.add(classScxMapping.value());
        }
        //??????????????? url
        if (methodScxMapping.useNameAsUrl() && "".equals(methodScxMapping.value())) {
            urlList.add(CaseUtils.toKebab(method.getName()));
        } else {
            urlList.add(methodScxMapping.value());
        }
        return StringUtils.clearHttpUrl(urlList.toArray(new String[0]));
    }

    private int getOrder() {
        var o = 0;
        char[] chars = url.toCharArray();
        for (char aChar : chars) {
            if (aChar == ':') {
                o = o + 1;
            } else if (aChar == '*') {
                o = o + 2;
            }
        }
        return o;
    }

    /**
     * <p>getResult.</p>
     *
     * @param ctx a {@link io.vertx.ext.web.RoutingContext} object.
     * @return a {@link java.lang.Object} object.
     * @throws java.lang.Exception if any.
     */
    private Object getResult(RoutingContext ctx) throws Exception {
        Set<FileUpload> uploadFiles = ctx.get("uploadFiles");
        if (uploadFiles == null) {
            uploadFiles = new HashSet<>();
        }
        var parameters = method.getParameters();
        //?????????????????????????????? ???????????????
        var jsonNode = ObjectUtils.JsonToTree(ctx.request().method() != HttpMethod.GET ? ctx.getBodyAsString() : "");
        var formAttributes = multiMapToMap(ctx.request().formAttributes());
        var queryParams = multiMapToMap(ctx.queryParams());
        var pathParams = ctx.pathParams();

        var finalHandlerParams = new Object[parameters.length];
        for (int i = 0; i < finalHandlerParams.length; i++) {
            var nowType = parameters[i].getType();
            if (nowType == RoutingContext.class) {
                finalHandlerParams[i] = ctx;
                continue;
            }
            if (nowType == DeviceType.class) {
                finalHandlerParams[i] = ScxAuth.getDevice(ctx);
                continue;
            }
            if (nowType == FileUpload.class) {
                String name = parameters[i].getName();
                finalHandlerParams[i] = uploadFiles.stream().filter(c -> name.equals(c.name)).findAny().orElse(null);
                continue;
            }
            var bodyParam = parameters[i].getAnnotation(FromBody.class);
            if (bodyParam != null) {
                finalHandlerParams[i] = getParamFromBody(jsonNode, formAttributes, bodyParam.value(), parameters[i], bodyParam.required());
                continue;
            }
            var queryParam = parameters[i].getAnnotation(FromQuery.class);
            if (queryParam != null) {
                finalHandlerParams[i] = getParamFromMap(queryParams, queryParam.value(), queryParam.merge(), parameters[i], queryParam.required());
                continue;
            }
            var pathParam = parameters[i].getAnnotation(FromPath.class);
            if (pathParam != null) {
                finalHandlerParams[i] = getParamFromMap(pathParams, pathParam.value(), pathParam.merge(), parameters[i], pathParam.required());
                continue;
            }
            //------ ??????????????????????????????????????????????????? ---------------
            //  ??? body ??????????????? ????????? ??????????????????????????????
            finalHandlerParams[i] = getParamFromBody(jsonNode, formAttributes, parameters[i].getName(), parameters[i], false);
            if (finalHandlerParams[i] != null) {
                continue;
            }
            // ??????????????????????????? ??????
            finalHandlerParams[i] = getParamFromBody(jsonNode, formAttributes, null, parameters[i], false);
            if (finalHandlerParams[i] != null) {
                continue;
            }
            //??????????????????????????????
            finalHandlerParams[i] = getParamFromMap(queryParams, parameters[i].getName(), false, parameters[i], false);
            if (finalHandlerParams[i] != null) {
                continue;
            }
            //?????????????????????
            finalHandlerParams[i] = getParamFromMap(pathParams, parameters[i].getName(), false, parameters[i], false);
        }
        return method.invoke(example, finalHandlerParams);
    }

    private Set<HttpMethod> getHttpMethod() {
        return Stream.of(methodScxMapping.method())
                .map(r -> HttpMethod.valueOf(r.toString()))
                .collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     * <p>
     * handle
     */
    @Override
    public void handle(RoutingContext context) {
        ScxContext.routingContext(context);
        try {
            //?????????????????? ????????????????????????
            if (!checkedLoginAndPerms(context)) {
                return;
            }

            var result = getResult(context);

            var response = context.response();
            if (result instanceof String || result instanceof Integer || result instanceof Double || result instanceof Boolean) {
                response.putHeader("Content-Type", "text/plain; charset=utf-8");
                response.end(result.toString());
            } else if (result instanceof BaseVo) {
                ((BaseVo) result).sendToClient(context);
            } else {
                response.end(ObjectUtils.beanToJsonUseAnnotations(result));
            }

        } catch (Exception e) {
            exceptionHandler0(e, context);
        }
    }

}
