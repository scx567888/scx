package cool.scx.jdbc.result_handler;

import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.result_handler.bean_builder.BeanBuilder;
import cool.scx.jdbc.result_handler.map_builder.MapBuilder;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static cool.scx.jdbc.dialect.DefaultDialect.DEFAULT_DIALECT;

/// ResultHandler
///
/// @author scx567888
/// @version 0.0.1
public interface ResultHandler<T> {

    static ResultHandler<Map<String, Object>> ofMap() {
        return MapHandler.INSTANCE;
    }

    static ResultHandler<Map<String, Object>> ofMap(MapBuilder mapBuilder) {
        return new MapHandler(mapBuilder);
    }

    static ResultHandler<Map<String, Object>> ofMap(Supplier<Map<String, Object>> mapSupplier) {
        return new MapHandler(MapBuilder.of(mapSupplier));
    }

    static ResultHandler<Map<String, Object>> ofMap(Function<String, String> fieldNameMapping) {
        return new MapHandler(MapBuilder.of(fieldNameMapping));
    }

    static ResultHandler<List<Map<String, Object>>> ofMapList() {
        return MapListHandler.INSTANCE;
    }

    static ResultHandler<List<Map<String, Object>>> ofMapList(MapBuilder mapBuilder) {
        return new MapListHandler(mapBuilder);
    }

    static ResultHandler<List<Map<String, Object>>> ofMapList(Supplier<Map<String, Object>> mapSupplier) {
        return new MapListHandler(MapBuilder.of(mapSupplier));
    }

    static ResultHandler<List<Map<String, Object>>> ofMapList(Function<String, String> fieldNameMapping) {
        return new MapListHandler(MapBuilder.of(fieldNameMapping));
    }

    static <C> ResultHandler<C> ofBean(Class<C> clazz) {
        return new BeanHandler<>(BeanBuilder.of(clazz));
    }

    static <C> ResultHandler<C> ofBean(Class<C> clazz, Function<Field, String> columnNameMapping) {
        return new BeanHandler<>(BeanBuilder.of(clazz, columnNameMapping));
    }

    static <C> ResultHandler<C> ofBean(BeanBuilder<C> beanBuilder) {
        return new BeanHandler<>(beanBuilder);
    }

    static <C> ResultHandler<List<C>> ofBeanList(Class<C> clazz) {
        return new BeanListHandler<>(BeanBuilder.of(clazz));
    }

    static <C> ResultHandler<List<C>> ofBeanList(Class<C> clazz, Function<Field, String> columnNameMapping) {
        return new BeanListHandler<>(BeanBuilder.of(clazz, columnNameMapping));
    }

    static <C> ResultHandler<List<C>> ofBeanList(BeanBuilder<C> beanBuilder) {
        return new BeanListHandler<>(beanBuilder);
    }

    static <C> ResultHandler<Void> ofBeanConsumer(Class<C> clazz, Consumer<C> consumer) {
        return new BeanConsumerHandler<>(BeanBuilder.of(clazz), consumer);
    }

    static <C> ResultHandler<Void> ofBeanConsumer(Class<C> clazz, Function<Field, String> columnNameMapping, Consumer<C> consumer) {
        return new BeanConsumerHandler<>(BeanBuilder.of(clazz, columnNameMapping), consumer);
    }

    static <C> ResultHandler<Void> ofBeanConsumer(BeanBuilder<C> beanBuilder, Consumer<C> consumer) {
        return new BeanConsumerHandler<>(beanBuilder, consumer);
    }

    static ResultHandler<Void> ofMapConsumer(Consumer<Map<String, Object>> consumer) {
        return new MapConsumerHandler(MapBuilder.of(), consumer);
    }

    static ResultHandler<Void> ofMapConsumer(MapBuilder mapBuilder, Consumer<Map<String, Object>> consumer) {
        return new MapConsumerHandler(mapBuilder, consumer);
    }

    static ResultHandler<Void> ofMapConsumer(Supplier<Map<String, Object>> mapSupplier, Consumer<Map<String, Object>> consumer) {
        return new MapConsumerHandler(MapBuilder.of(mapSupplier), consumer);
    }

    static ResultHandler<Void> ofMapConsumer(Function<String, String> fieldNameMapping, Consumer<Map<String, Object>> consumer) {
        return new MapConsumerHandler(MapBuilder.of(fieldNameMapping), consumer);
    }

    static <C> ResultHandler<C> ofSingleValue(String columnName, Class<C> clazz) {
        return new SingleValueHandler<>(columnName, clazz);
    }

    static <C> ResultHandler<C> ofSingleValue(int columnIndex, Class<C> clazz) {
        return new SingleValueHandler<>(columnIndex, clazz);
    }

    T apply(ResultSet rs, Dialect dialect) throws SQLException;

    default T apply(ResultSet rs) throws SQLException {
        return apply(rs, DEFAULT_DIALECT);
    }

}
