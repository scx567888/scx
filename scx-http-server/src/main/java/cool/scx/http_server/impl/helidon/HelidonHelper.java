package cool.scx.http_server.impl.helidon;

import io.helidon.common.mapper.OptionalValue;
import io.helidon.common.mapper.Value;
import io.helidon.common.parameters.Parameters;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public class HelidonHelper {


    public static class FormDataBuilder implements Parameters {

        @Override
        public List<String> all(String s) throws NoSuchElementException {
            return List.of();
        }

        @Override
        public List<Value<String>> allValues(String s) throws NoSuchElementException {
            return List.of();
        }

        @Override
        public String get(String s) throws NoSuchElementException {
            return "";
        }

        @Override
        public OptionalValue<String> first(String s) {
            return null;
        }

        @Override
        public boolean contains(String s) {
            return false;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public Set<String> names() {
            return Set.of();
        }

        @Override
        public String component() {
            return "";
        }

    }
    
    
}
