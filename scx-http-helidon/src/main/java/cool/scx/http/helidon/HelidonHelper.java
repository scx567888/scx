package cool.scx.http.helidon;

import cool.scx.http.PeerInfo;
import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.ScxURIWritable;
import cool.scx.reflect.ReflectFactory;
import io.helidon.http.Header;
import io.helidon.http.Headers;
import io.helidon.http.HttpPrologue;
import io.helidon.http.PathMatchers;
import io.helidon.webserver.websocket.WsRoute;
import io.helidon.webserver.websocket.WsRouting;
import io.helidon.websocket.WsListener;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * HelidonHelper
 */
class HelidonHelper {

    public static WsRouting.Builder createHelidonWebSocketRouting(HelidonHttpServer server) {
        try {
            var wsRouteClassInfo = ReflectFactory.getClassInfo(WsRoute.class);
            var constructor = wsRouteClassInfo.constructors()[0];
            constructor.setAccessible(true);
            var wsRoute = constructor.newInstance(PathMatchers.any(), (Supplier<WsListener>) () -> new HelidonServerWebSocket(server));
            var builder = WsRouting.builder();
            var classInfo = ReflectFactory.getClassInfo(WsRouting.Builder.class);
            var routeMethod = Arrays.stream(classInfo.methods()).filter(method -> method.name().equals("route")).findFirst().orElseThrow();
            routeMethod.setAccessible(true);
            routeMethod.method().invoke(builder, wsRoute);
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ScxHttpHeadersWritable convertHeaders(Headers o) {
        var h = ScxHttpHeaders.of();
        for (Header header : o) {
            h.add(header.name(), header.allValues().toArray(new String[0]));
        }
        return h;
    }

    public static ScxURIWritable createScxURI(HttpPrologue p) {
        var path = p.uriPath().path();
        var query = p.query().value();
        var fragment = p.fragment().hasValue() ? p.fragment().value() : null;
        return ScxURI.of()
                .path(path)
                .query(query)
                .fragment(fragment);
    }

    public static PeerInfo convertPeerInfo(io.helidon.common.socket.PeerInfo peerInfo) {
        return PeerInfo.of()
                .address(peerInfo.address())
                .host(peerInfo.host())
                .port(peerInfo.port())
                .tlsPrincipal(peerInfo.tlsPrincipal().orElse(null))
                .tlsCertificates(peerInfo.tlsCertificates().orElse(null));
    }

}
