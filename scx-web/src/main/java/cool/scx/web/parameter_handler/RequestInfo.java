package cool.scx.web.parameter_handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import cool.scx.common.multi_map.MultiMap;
import cool.scx.http.media.multi_part.MultiPartPart;
import cool.scx.http.media_type.ScxMediaType;
import cool.scx.http.routing.RoutingContext;

import static cool.scx.common.util.ObjectUtils.jsonMapper;
import static cool.scx.common.util.ObjectUtils.xmlMapper;
import static cool.scx.http.media_type.MediaType.*;

/// 封装 RoutingContext 的参数 防止反复取值造成性能损失
///
/// @author scx567888
/// @version 0.0.1
public final class RequestInfo {

    private final RoutingContext routingContext;
    private final ScxMediaType contentType;
    private final JsonNode pathParams;
    private final JsonNode query;
    private final boolean cachedMultiPart;
    private JsonNode body;
    private MultiMap<String, MultiPartPart> uploadFiles;
    private boolean bodyInit;

    public RequestInfo(RoutingContext ctx, boolean cachedMultiPart) {
        this.routingContext = ctx;
        this.cachedMultiPart = cachedMultiPart;
        this.contentType = ctx.request().contentType();
        this.pathParams = jsonMapper().convertValue(ctx.pathParams().toMap(), JsonNode.class);
        this.query = jsonMapper().convertValue(ctx.request().query().toMap(), JsonNode.class);
        this.bodyInit = false;
    }

    /// 走到这里标识以上的匹配全部失败 , 这里不知道 body 的具体格式 所以进行猜测转换
    ///
    /// @param str a
    /// @return a
    public static JsonNode tryReadOrTextNode(String str) {
        try { //先尝试以 json 格式进行尝试转换
            return jsonMapper().readTree(str);
        } catch (Exception exception) {
            try {//再尝试以 xml 的格式进行转换
                return xmlMapper().readTree(str);
            } catch (JsonProcessingException e) {
                // json 和 xml 均转换失败 直接存储 为 string
                return new TextNode(str);
            }
        }
    }

    /// 根据不同的 ContentType 以不同的逻辑初始化 body
    ///
    /// @param ctx         ctx
    /// @param contentType a
    private void initBody(RoutingContext ctx, ScxMediaType contentType) {
        bodyInit = true;
        // 除了 MULTIPART_FORM_DATA 其余全部转为 JsonNode 的形式方便后续使用
        if (APPLICATION_JSON.equalsIgnoreParams(contentType) || APPLICATION_XML.equalsIgnoreParams(contentType)) {
            this.body = ctx.request().body().asJsonNode();
            return;
        }
        if (APPLICATION_X_WWW_FORM_URLENCODED.equalsIgnoreParams(contentType)) {
            var formParams = ctx.request().body().asFormParams();
            this.body = jsonMapper().convertValue(formParams.toMap(), JsonNode.class);
            return;
        }
        if (MULTIPART_FORM_DATA.equalsIgnoreParams(contentType)) {
            //这里我们分为两类
            var m = new MultiMap<String, String>();
            var f = new MultiMap<String, MultiPartPart>();
            //文件和非文件
            var multiPart = cachedMultiPart ? ctx.request().body().asMultiPartCached() : ctx.request().body().asMultiPart();
            for (var multiPartPart : multiPart) {
                //没有文件名我们就当成 空文件
                if (multiPartPart.filename() == null) {
                    m.add(multiPartPart.name(), multiPartPart.asString());
                }
                f.add(multiPartPart.name(), multiPartPart);
            }
            this.body = jsonMapper().convertValue(m.toMultiValueMap(), JsonNode.class);
            this.uploadFiles = f;
            return;
        }
        var string = ctx.request().body().asString();
        this.body = string != null ? tryReadOrTextNode(string) : null;
    }

    public JsonNode pathParams() {
        return pathParams;
    }

    public JsonNode query() {
        return query;
    }

    public JsonNode body() {
        if (!bodyInit) {
            initBody(this.routingContext, this.contentType);
        }
        return body;
    }

    public MultiMap<String, MultiPartPart> uploadFiles() {
        if (!bodyInit) {
            initBody(this.routingContext, this.contentType);
        }
        return uploadFiles;
    }

    public ScxMediaType contentType() {
        return contentType;
    }

    public RoutingContext routingContext() {
        return routingContext;
    }

}
