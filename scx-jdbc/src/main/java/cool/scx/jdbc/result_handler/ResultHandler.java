package cool.scx.jdbc.result_handler;

import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.dialect._default.DefaultDialect;
import cool.scx.jdbc.result_handler.bean_builder.BeanBuilder;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <p>ResultHandler interface.</p>
 *
 * @author scx567888
 * @version 0.0.7
 */
public interface ResultHandler<T> {

    static ResultHandler<Map<String, Object>> ofMap() {
        return MapHandler.INSTANCE;
    }

    static ResultHandler<Map<String, Object>> ofMap(Supplier<Map<String, Object>> mapSupplier) {
        return new MapHandler(mapSupplier);
    }

    static ResultHandler<List<Map<String, Object>>> ofMapList() {
        return MapListHandler.INSTANCE;
    }

    static ResultHandler<List<Map<String, Object>>> ofMapList(Supplier<Map<String, Object>> mapSupplier) {
        return new MapListHandler(mapSupplier);
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
        return new MapConsumerHandler(consumer);
    }

    static ResultHandler<Void> ofMapConsumer(Supplier<Map<String, Object>> mapSupplier, Consumer<Map<String, Object>> consumer) {
        return new MapConsumerHandler(mapSupplier, consumer);
    }

    static <C> ResultHandler<C> ofSingleValue(String columnName, Class<C> clazz) {
        return new SingleValueHandler<>(columnName, clazz);
    }

    static <C> ResultHandler<C> ofSingleValue(int columnIndex, Class<C> clazz) {
        return new SingleValueHandler<>(columnIndex, clazz);
    }

    T apply(ResultSet rs, Dialect dialect) throws SQLException;

    default T apply(ResultSet rs) throws SQLException {
        return apply(rs, DefaultDialect.DEFAULT_DIALECT);
    }

}
