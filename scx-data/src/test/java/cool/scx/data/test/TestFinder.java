package cool.scx.data.test;

import cool.scx.data.Finder;
import cool.scx.data.exception.DataAccessException;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TestFinder<T> implements Finder<T> {

    @Override
    public List<T> list() throws DataAccessException {
        return List.of();
    }

    @Override
    public <T1> List<T1> list(Class<T1> resultType) throws DataAccessException {
        return List.of();
    }

    @Override
    public List<Map<String, Object>> listMap() throws DataAccessException {
        return List.of();
    }

    @Override
    public void forEach(Consumer<T> tConsumer) throws DataAccessException {

    }

    @Override
    public <T1> void forEach(Consumer<T1> entityConsumer, Class<T1> resultType) throws DataAccessException {

    }

    @Override
    public void forEachMap(Consumer<Map<String, Object>> entityConsumer) throws DataAccessException {

    }

    @Override
    public T first() throws DataAccessException {
        return null;
    }

    @Override
    public <T1> T1 first(Class<T1> resultType) throws DataAccessException {
        return null;
    }

    @Override
    public Map<String, Object> firstMap() throws DataAccessException {
        return Map.of();
    }

    @Override
    public long count() throws DataAccessException {
        return 0;
    }
    
}
