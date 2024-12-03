package cool.scx.config.source;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.scx.common.jackson.JsonNodeHelper;
import cool.scx.config.ScxConfigSource;

/**
 * MultiConfigSource
 *
 * @author scx567888
 * @version 0.0.1
 */
public class MultiConfigSource extends AbstractConfigSource {

    private final ScxConfigSource[] sources;

    public MultiConfigSource(ScxConfigSource... sources) {
        this.sources = sources;
        this.configMapping = loadFromSources(sources);
        bindOnChange(sources);
    }

    public static ObjectNode loadFromSources(ScxConfigSource... sources) {
        var configMapping = JsonNodeFactory.instance.objectNode();
        for (var scxConfigSource : sources) {
            JsonNodeHelper.merge(configMapping, scxConfigSource.configMapping());
        }
        return configMapping;
    }

    public void bindOnChange(ScxConfigSource... sources) {
        for (var source : sources) {
            source.onChange(this::callOnChange0);
        }
    }

    private void callOnChange0(ObjectNode oldValue, ObjectNode newValue) {
        var oldConfigMapping = this.configMapping;
        this.configMapping = loadFromSources(sources);
        callOnChange(oldConfigMapping, this.configMapping);
    }

}
