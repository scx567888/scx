package cool.scx.jdbc.result_handler;

import cool.scx.function.Function1Void;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.result_handler.bean_builder.BeanBuilder;
import cool.scx.jdbc.result_handler.map_builder.MapBuilder;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static cool.scx.jdbc.dialect.DefaultDialect.DEFAULT_DIALECT;

/// ResultHandler
///
/// @author scx567888
/// @version 0.0.1
public interface ResultHandler<T, X extends Throwable> {

    static ResultHandler<Map<String, Object>, RuntimeException> ofMap() {
        return MapHandler.INSTANCE;
    }

    static ResultHandler<Map<String, Object>, RuntimeException> ofMap(MapBuilder mapBuilder) {
        return new MapHandler(mapBuilder);
    }

    static ResultHandler<Map<String, Object>, RuntimeException> ofMap(Supplier<Map<String, Object>> mapSupplier) {
        return new MapHandler(MapBuilder.of(mapSupplier));
    }

    static ResultHandler<Map<String, Object>, RuntimeException> ofMap(Function<String, String> fieldNameMapping) {
        return new MapHandler(MapBuilder.of(fieldNameMapping));
    }

    static ResultHandler<List<Map<String, Object>>, RuntimeException> ofMapList() {
        return MapListHandler.INSTANCE;
    }

    static ResultHandler<List<Map<String, Object>>, RuntimeException> ofMapList(MapBuilder mapBuilder) {
        return new MapListHandler(mapBuilder);
    }

    static ResultHandler<List<Map<String, Object>>, RuntimeException> ofMapList(Supplier<Map<String, Object>> mapSupplier) {
        return new MapListHandler(MapBuilder.of(mapSupplier));
    }

    static ResultHandler<List<Map<String, Object>>, RuntimeException> ofMapList(Function<String, String> fieldNameMapping) {
        return new MapListHandler(MapBuilder.of(fieldNameMapping));
    }

    static <C> ResultHandler<C, RuntimeException> ofBean(Class<C> clazz) {
        return new BeanHandler<>(BeanBuilder.of(clazz));
    }

    static <C> ResultHandler<C, RuntimeException> ofBean(Class<C> clazz, Function<Field, String> columnNameMapping) {
        return new BeanHandler<>(BeanBuilder.of(clazz, columnNameMapping));
    }

    static <C> ResultHandler<C, RuntimeException> ofBean(BeanBuilder<C> beanBuilder) {
        return new BeanHandler<>(beanBuilder);
    }

    static <C> ResultHandler<List<C>, RuntimeException> ofBeanList(Class<C> clazz) {
        return new BeanListHandler<>(BeanBuilder.of(clazz));
    }

    static <C> ResultHandler<List<C>, RuntimeException> ofBeanList(Class<C> clazz, Function<Field, String> columnNameMapping) {
        return new BeanListHandler<>(BeanBuilder.of(clazz, columnNameMapping));
    }

    static <C> ResultHandler<List<C>, RuntimeException> ofBeanList(BeanBuilder<C> beanBuilder) {
        return new BeanListHandler<>(beanBuilder);
    }

    static <C, X extends Throwable> ResultHandler<Void, X> ofBeanConsumer(Class<C> clazz, Function1Void<C, X> consumer) {
        return new BeanConsumerHandler<>(BeanBuilder.of(clazz), consumer);
    }

    static <C, X extends Throwable> ResultHandler<Void, X> ofBeanConsumer(Class<C> clazz, Function<Field, String> columnNameMapping, Function1Void<C, X> consumer) {
        return new BeanConsumerHandler<>(BeanBuilder.of(clazz, columnNameMapping), consumer);
    }

    static <C, X extends Throwable> ResultHandler<Void, X> ofBeanConsumer(BeanBuilder<C> beanBuilder, Function1Void<C, X> consumer) {
        return new BeanConsumerHandler<>(beanBuilder, consumer);
    }

    static <X extends Throwable> ResultHandler<Void, X> ofMapConsumer(Function1Void<Map<String, Object>, X> consumer) {
        return new MapConsumerHandler<>(MapBuilder.of(), consumer);
    }

    static <X extends Throwable> ResultHandler<Void, X> ofMapConsumer(MapBuilder mapBuilder, Function1Void<Map<String, Object>, X> consumer) {
        return new MapConsumerHandler<>(mapBuilder, consumer);
    }

    static <X extends Throwable> ResultHandler<Void, X> ofMapConsumer(Supplier<Map<String, Object>> mapSupplier, Function1Void<Map<String, Object>, X> consumer) {
        return new MapConsumerHandler<>(MapBuilder.of(mapSupplier), consumer);
    }

    static <X extends Throwable> ResultHandler<Void, X> ofMapConsumer(Function<String, String> fieldNameMapping, Function1Void<Map<String, Object>, X> consumer) {
        return new MapConsumerHandler<>(MapBuilder.of(fieldNameMapping), consumer);
    }

    static <C> ResultHandler<C, RuntimeException> ofSingleValue(String columnName, Class<C> clazz) {
        return new SingleValueHandler<>(columnName, clazz);
    }

    static <C> ResultHandler<C, RuntimeException> ofSingleValue(int columnIndex, Class<C> clazz) {
        return new SingleValueHandler<>(columnIndex, clazz);
    }

    T apply(ResultSet rs, Dialect dialect) throws SQLException, X;

    default T apply(ResultSet rs) throws SQLException, X {
        return apply(rs, DEFAULT_DIALECT);
    }

}
