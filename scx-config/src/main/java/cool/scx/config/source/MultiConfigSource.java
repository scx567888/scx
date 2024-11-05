package cool.scx.config.source;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.scx.common.util.JsonNodeHelper;
import cool.scx.config.ScxConfigSource;

public class MultiConfigSource extends AbstractConfigSource {

    private final ScxConfigSource[] sources;
    protected ObjectNode configMapping;

    public MultiConfigSource(ScxConfigSource... sources) {
        this.sources = sources;
        this.configMapping = loadFromSources(sources);
        bindOnChange(sources);
    }

    public void bindOnChange(ScxConfigSource... sources) {
        for (var source : sources) {
            source.onChange(this::callOnChange0);
        }
    }

    private void callOnChange0(ObjectNode objectNode) {
        this.configMapping = loadFromSources(sources);
        callOnChange(this.configMapping);
    }

    public static ObjectNode loadFromSources(ScxConfigSource... sources) {
        var configMapping = JsonNodeFactory.instance.objectNode();
        for (var scxConfigSource : sources) {
            JsonNodeHelper.merge(configMapping, scxConfigSource.configMapping());
        }
        return configMapping;
    }

    @Override
    public ObjectNode configMapping() {
        return configMapping;
    }

}
