package cool.scx.config.source;

import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.scx.config.ScxConfigSource;

import java.util.function.Consumer;

public abstract class AbstractConfigSource implements ScxConfigSource {

    private Consumer<ObjectNode> changeHandler;

    @Override
    public void onChange(Consumer<ObjectNode> changeHandler) {
        this.changeHandler = changeHandler;
    }

    public void callOnChange(ObjectNode configMapping) {
        if (changeHandler != null) {
            this.changeHandler.accept(configMapping);
        }
    }

}
