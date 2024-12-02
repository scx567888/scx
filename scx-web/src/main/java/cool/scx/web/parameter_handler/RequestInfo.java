package cool.scx.web.parameter_handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import cool.scx.common.multi_map.MultiMap;
import cool.scx.http.content_type.ContentType;
import cool.scx.http.exception.BadRequestException;
import cool.scx.http.media.multi_part.MultiPartPart;
import cool.scx.http.routing.RoutingContext;

import static cool.scx.common.util.ObjectUtils.jsonMapper;
import static cool.scx.common.util.ObjectUtils.xmlMapper;
import static cool.scx.http.MediaType.*;

/**
 * 封装 RoutingContext 的参数 防止反复取值造成性能损失
 *
 * @author scx567888
 * @version 1.4.7
 */
public final class RequestInfo {

    private final RoutingContext routingContext;
    private final ContentType contentType;
    private JsonNode body;
    private JsonNode pathParams;
    private JsonNode query;
    private MultiMap<String, MultiPartPart> uploadFiles;

    public RequestInfo(RoutingContext ctx) {
        this.routingContext = ctx;
        this.contentType = ctx.request().contentType();
        this.pathParams = jsonMapper().convertValue(ctx.pathParams().toMap(), JsonNode.class);
        this.query = jsonMapper().convertValue(ctx.request().query().toMap(), JsonNode.class);
        initBody(ctx, this.contentType);
    }

    public static JsonNode readJson(String jsonStr) {
        try {
            return jsonMapper().readTree(jsonStr);
        } catch (JsonProcessingException e) {
            throw new BadRequestException(e);
        }
    }

    public static JsonNode readXml(String xmlStr) {
        try {
            return xmlMapper().readTree(xmlStr);
        } catch (JsonProcessingException e) {
            throw new BadRequestException(e);
        }
    }

    /**
     * 走到这里标识以上的匹配全部失败 , 这里不知道 body 的具体格式 所以进行猜测转换
     *
     * @param str a
     * @return a
     */
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

    /**
     * 根据不同的 ContentType 以不同的逻辑初始化 body
     *
     * @param ctx         ctx
     * @param contentType a
     */
    private void initBody(RoutingContext ctx, ContentType contentType) {
        var mediaType = contentType != null ? contentType.mediaType() : null;
        // 除了 MULTIPART_FORM_DATA 其余全部转为 JsonNode 的形式方便后续使用
        switch (mediaType) {
            case APPLICATION_JSON -> {
                var string = ctx.request().body().asString();
                this.body = readJson(string);
            }
            case APPLICATION_XML -> {
                var string = ctx.request().body().asString();
                this.body = readXml(string);
            }
            case APPLICATION_X_WWW_FORM_URLENCODED -> {
                var formParams = ctx.request().body().asFormParams();
                this.body = jsonMapper().convertValue(formParams.toMap(), JsonNode.class);
            }
            case MULTIPART_FORM_DATA -> {
                //这里我们分为两类
                var m = new MultiMap<String, String>();
                var f = new MultiMap<String, MultiPartPart>();
                //文件和非文件
                var multiPart = ctx.request().body().asMultiPartCached();
                for (var multiPartPart : multiPart) {
                    //没有文件名我们就当成 空文件
                    if (multiPartPart.filename() == null) {
                        m.add(multiPartPart.name(), multiPartPart.asString());
                    }
                    f.add(multiPartPart.name(), multiPartPart);
                }
                this.body = jsonMapper().convertValue(m.toMultiValueMap(), JsonNode.class);
                this.uploadFiles = f;
            }
            case null, default -> {
                var string = ctx.request().body().asString();
                this.body = string != null ? tryReadOrTextNode(string) : null;
            }
        }
    }

    public JsonNode pathParams() {
        return pathParams;
    }

    public JsonNode query() {
        return query;
    }

    public JsonNode body() {
        return body;
    }

    public MultiMap<String, MultiPartPart> uploadFiles() {
        return uploadFiles;
    }

    public ContentType contentType() {
        return contentType;
    }

    public RoutingContext routingContext() {
        return routingContext;
    }

}
