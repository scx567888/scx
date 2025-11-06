package cool.scx.web.parameter_handler;

import cool.scx.collections.multi_map.MultiMap;
import cool.scx.http.media.multi_part.MultiPartPart;
import cool.scx.http.media.multi_part.MultiPartPartImpl;
import cool.scx.http.media_type.ScxMediaType;
import cool.scx.http.routing.RoutingContext;
import cool.scx.io.exception.ScxIOException;
import cool.scx.object.ScxObject;
import cool.scx.object.node.Node;
import cool.scx.object.node.ObjectNode;
import cool.scx.object.node.TextNode;
import cool.scx.object.parser.NodeParseException;

import java.io.IOException;

import static cool.scx.http.media_type.MediaType.*;
import static cool.scx.object.ScxObject.valueToNode;

/// 封装 RoutingContext 的参数 防止反复取值造成性能损失
///
/// @author scx567888
/// @version 0.0.1
public final class RequestInfo {

    private final RoutingContext routingContext;
    private final ScxMediaType contentType;
    private final ObjectNode pathParams;
    private final ObjectNode query;
    private final boolean cachedMultiPart;
    private Node body;
    private MultiMap<String, MultiPartPart> uploadFiles;
    private boolean bodyInit;

    public RequestInfo(RoutingContext ctx, boolean cachedMultiPart) {
        this.routingContext = ctx;
        this.cachedMultiPart = cachedMultiPart;
        this.contentType = ctx.request().contentType();
        this.pathParams = (ObjectNode) valueToNode(ctx.pathParams().toMultiValueMap());
        this.query = (ObjectNode) valueToNode(ctx.request().query().toMultiValueMap());
        this.bodyInit = false;
    }

    /// 走到这里标识以上的匹配全部失败 , 这里不知道 body 的具体格式 所以进行猜测转换
    ///
    /// @param str a
    /// @return a
    public static Node tryReadOrTextNode(String str) {
        try { //先尝试以 json 格式进行尝试转换
            return ScxObject.fromJson(str);
        } catch (NodeParseException exception) {
            try {//再尝试以 xml 的格式进行转换
                return ScxObject.fromXml(str);
            } catch (NodeParseException e) {
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
            this.body = ctx.request().body().asNode();
            return;
        }
        if (APPLICATION_X_WWW_FORM_URLENCODED.equalsIgnoreParams(contentType)) {
            var formParams = ctx.request().body().asFormParams();
            this.body = valueToNode(formParams.toMultiValueMap());
            return;
        }
        if (MULTIPART_FORM_DATA.equalsIgnoreParams(contentType)) {
            //这里我们分为两类
            var m = new MultiMap<String, String>();
            var f = new MultiMap<String, MultiPartPart>();
            //文件和非文件
            var multiPart = ctx.request().body().asMultiPart();
            for (var multiPartPart : multiPart) {
                //没有文件名我们就当成 空文件
                if (multiPartPart.filename() == null) {
                    m.add(multiPartPart.name(), multiPartPart.asString());
                }
                try {
                    //这里我们需要将流式的读取到内存中
                    // todo 这里怎么处理 AutoClose ?
                    var bytes = multiPartPart.byteInput().readAll();
                    f.add(multiPartPart.name(), new MultiPartPartImpl().headers(multiPartPart.headers()).body(bytes));
                } catch (ScxIOException e) {
                    throw new RuntimeException(e);
                }
            }
            this.body = valueToNode(m.toMultiValueMap());
            this.uploadFiles = f;
            return;
        }
        var string = ctx.request().body().asString();
        this.body = string != null ? tryReadOrTextNode(string) : null;
    }

    public ObjectNode pathParams() {
        return pathParams;
    }

    public ObjectNode query() {
        return query;
    }

    public Node body() {
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
