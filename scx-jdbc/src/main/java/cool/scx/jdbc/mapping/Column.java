package cool.scx.jdbc.mapping;

/// 列
///
/// @author scx567888
/// @version 0.0.1
public interface Column {

    default String table() {
        return null;
    }

    String name();

    DataType dataType();

    String defaultValue();

    String onUpdate();

    boolean notNull();

    boolean autoIncrement();

    boolean primary();

    boolean unique();

    boolean index();

    default String comment() {
        return null;
    }

}
