package cool.scx.http.x.http1.connection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static cool.scx.http.media_type.ScxMediaTypeHelper.SEMICOLON_PATTERN;
import static java.util.Collections.addAll;

/// todo 待优化
public class Connection implements Iterable<ScxConnectionType> {

    private final List<ScxConnectionType> connectionTypes;

    public Connection(List<ScxConnectionType> connectionTypes) {
        this.connectionTypes = connectionTypes;
    }

    public Connection(ScxConnectionType... connectionTypes) {
        this.connectionTypes = new ArrayList<>(connectionTypes.length);
        addAll(this.connectionTypes, connectionTypes);
    }

    public static Connection parseConnection(String connectionHeader) {
        var split = SEMICOLON_PATTERN.split(connectionHeader);
        var list = new ArrayList<ScxConnectionType>();
        for (var s : split) {
            list.add(ScxConnectionType.of(s.trim()));
        }
        return new Connection(list);
    }

    public List<ScxConnectionType> connectionTypes() {
        return connectionTypes;
    }

    @Override
    public Iterator<ScxConnectionType> iterator() {
        return connectionTypes.iterator();
    }

    public String encode() {
        return connectionTypes.stream().map(ScxConnectionType::value).collect(Collectors.joining(", "));
    }

    public int size() {
        return connectionTypes.size();
    }

    public ScxConnectionType get(int i) {
        return connectionTypes.get(i);
    }

    public boolean contains(ScxConnectionType connectionType) {
        return connectionTypes.contains(connectionType);
    }

}
