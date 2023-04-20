package cool.scx.data.jdbc.dialect;

import cool.scx.data.jdbc.ColumnMapping;
import cool.scx.data.jdbc.mapping.Column;
import cool.scx.data.jdbc.mapping.Table;
import cool.scx.data.jdbc.type_handler.*;
import cool.scx.data.jdbc.type_handler.math.BigDecimalTypeHandler;
import cool.scx.data.jdbc.type_handler.math.BigIntegerTypeHandler;
import cool.scx.data.jdbc.type_handler.primitive.*;
import cool.scx.data.jdbc.type_handler.time.*;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static cool.scx.util.StringUtils.notEmpty;
import static cool.scx.util.reflect.ClassUtils.isEnum;

public abstract class Dialect {

    protected final Map<Type, TypeHandler<?>> TYPE_HANDLER_MAP = new ConcurrentHashMap<>();

    public Dialect() {
        initTypeHandler();
    }

    public void initTypeHandler() {
        // 基本类型
        TYPE_HANDLER_MAP.put(boolean.class, new BooleanTypeHandler(true));
        TYPE_HANDLER_MAP.put(char.class, new CharacterTypeHandler(true));
        TYPE_HANDLER_MAP.put(byte.class, new ByteTypeHandler(true));
        TYPE_HANDLER_MAP.put(short.class, new ShortTypeHandler(true));
        TYPE_HANDLER_MAP.put(int.class, new IntegerTypeHandler(true));
        TYPE_HANDLER_MAP.put(long.class, new LongTypeHandler(true));
        TYPE_HANDLER_MAP.put(float.class, new FloatTypeHandler(true));
        TYPE_HANDLER_MAP.put(double.class, new DoubleTypeHandler(true));
        TYPE_HANDLER_MAP.put(Boolean.class, new BooleanTypeHandler(false));
        TYPE_HANDLER_MAP.put(Character.class, new CharacterTypeHandler(false));
        TYPE_HANDLER_MAP.put(Byte.class, new ByteTypeHandler(false));
        TYPE_HANDLER_MAP.put(Short.class, new ShortTypeHandler(false));
        TYPE_HANDLER_MAP.put(Integer.class, new IntegerTypeHandler(false));
        TYPE_HANDLER_MAP.put(Long.class, new LongTypeHandler(false));
        TYPE_HANDLER_MAP.put(Float.class, new FloatTypeHandler(false));
        TYPE_HANDLER_MAP.put(Double.class, new DoubleTypeHandler(false));


        // 常用类型
        TYPE_HANDLER_MAP.put(String.class, new StringTypeHandler());
        TYPE_HANDLER_MAP.put(Byte[].class, new ByteObjectArrayTypeHandler());
        TYPE_HANDLER_MAP.put(byte[].class, new ByteArrayTypeHandler());


        // 大数字
        TYPE_HANDLER_MAP.put(BigInteger.class, new BigIntegerTypeHandler());
        TYPE_HANDLER_MAP.put(BigDecimal.class, new BigDecimalTypeHandler());


        // 时间
        TYPE_HANDLER_MAP.put(LocalDateTime.class, new LocalDateTimeTypeHandler());
        TYPE_HANDLER_MAP.put(LocalDate.class, new LocalDateTypeHandler());
        TYPE_HANDLER_MAP.put(LocalTime.class, new LocalTimeTypeHandler());
        TYPE_HANDLER_MAP.put(OffsetDateTime.class, new OffsetDateTimeTypeHandler());
        TYPE_HANDLER_MAP.put(OffsetTime.class, new OffsetTimeTypeHandler());
        TYPE_HANDLER_MAP.put(ZonedDateTime.class, new ZonedDateTimeTypeHandler());
        TYPE_HANDLER_MAP.put(Month.class, new MonthTypeHandler());
        TYPE_HANDLER_MAP.put(Year.class, new YearTypeHandler());
        TYPE_HANDLER_MAP.put(YearMonth.class, new YearMonthTypeHandler());
        TYPE_HANDLER_MAP.put(Date.class, new DateTypeHandler());
        TYPE_HANDLER_MAP.put(Instant.class, new InstantTypeHandler());
        TYPE_HANDLER_MAP.put(Duration.class, new DurationTypeHandler());

        //clob and blow
        TYPE_HANDLER_MAP.put(InputStream.class, new BlobInputStreamTypeHandler());
        TYPE_HANDLER_MAP.put(Reader.class, new ClobReaderTypeHandler());
    }

    /**
     * 是否可以处理
     *
     * @param dataSource 数据源
     * @return 是否可以处理
     */
    public abstract boolean canHandle(DataSource dataSource);

    /**
     * 是否可以处理
     *
     * @param driver 驱动
     * @return 是否可以处理
     */
    public abstract boolean canHandle(Driver driver);

    /**
     * 　获取最终的 SQL, 一般用于 Debug
     *
     * @param statement s
     * @return SQL 语句
     */
    public abstract String getFinalSQL(Statement statement);

    /**
     * 获取分页 SQL
     *
     * @param sql      原始 SQL
     * @param rowCount 行数
     * @param offset   偏移量
     * @return SQL 语句
     */
    public abstract String getLimitSQL(String sql, Long offset, Long rowCount);

    /**
     * 获取建表语句
     *
     * @return s
     */
    public String getCreateTableDDL(Table<?> tableInfo) {
        var s = new StringBuilder();
        s.append("CREATE TABLE ");
        if (notEmpty(tableInfo.schema())) {
            s.append(tableInfo.schema()).append(".");
        }
        s.append(tableInfo.name()).append("\n");
        s.append("(\n");

        // 创建子句
        var createDefinitionStr = getCreateDefinition(tableInfo).stream()
                .map(c -> "    " + c)
                .collect(Collectors.joining(",\n"));
        s.append(createDefinitionStr);

        s.append("\n);");
        return s.toString();
    }

    public List<String> getCreateDefinition(Table<?> table) {
        var createDefinitions = new ArrayList<String>();
        createDefinitions.addAll(getColumnDefinitions(table.columns()));
        createDefinitions.addAll(getTableConstraint(table));
        return createDefinitions;
    }

    public List<String> getColumnDefinitions(Column[] columns) {
        var list = new ArrayList<String>();
        for (var column : columns) {
            list.add(getColumnDefinition(column));
        }
        return list;
    }

    public List<String> getTableConstraint(Table<?> table) {
        return new ArrayList<>();
    }

    public String getColumnDefinition(Column column) {
        var s = new StringBuilder();
        s.append(column.name()).append(" ");// 列名
        var dataTypeDefinition = getDataTypeDefinition(column);
        if (dataTypeDefinition != null) {
            s.append(dataTypeDefinition).append(" ");
        }
        // 限制条件
        var columnConstraintStr = String.join(" ", getColumnConstraint(column));
        s.append(columnConstraintStr);
        return s.toString();
    }

    public String getDataTypeDefinition(Column column) {
        if (column.typeName() != null) {
            if (column.columnSize() != null) {
                return column.typeName() + "(" + column.columnSize() + ")";
            } else {
                return column.typeName();
            }
        } else {
            if (column instanceof ColumnMapping m) {
                return getDataTypeDefinitionByClass(m.javaField().getType());
            } else {
                return defaultDateType();
            }
        }
    }

    public abstract List<String> getColumnConstraint(Column columns);

    /**
     * 根据 class 获取对应的 SQLType 类型 如果没有则返回 JSON
     *
     * @param javaType 需要获取的类型
     * @return a {@link String} object.
     */
    public abstract String getDataTypeDefinitionByClass(Class<?> javaType);

    /**
     * 默认值
     *
     * @return 默认类型值
     */
    public String defaultDateType() {
        return null;
    }

    /**
     * todo
     *
     * @param needAdds  a
     * @param tableInfo a
     */
    public String getAlertTableDDL(Column[] needAdds, Table<?> tableInfo) {
        var s = new StringBuilder();
        s.append("ALTER TABLE ");
        if (notEmpty(tableInfo.schema())) {
            s.append(tableInfo.schema()).append(".");
        }
        s.append(tableInfo.name()).append("\n");

        var columnDefinitionStr = getColumnDefinitions(needAdds).stream()
                .map(c -> "    ADD COLUMN " + c)
                .collect(Collectors.joining(",\n"));

        s.append(columnDefinitionStr);
        s.append("\n;");
        return s.toString();
    }

    /**
     * 执行前
     *
     * @param preparedStatement a
     * @return a
     * @throws SQLException a
     */
    public PreparedStatement beforeExecuteQuery(PreparedStatement preparedStatement) throws SQLException {
        return preparedStatement;
    }

    @SuppressWarnings("unchecked")
    public final <T> TypeHandler<T> findTypeHandler(Type type) {
        return (TypeHandler<T>) TYPE_HANDLER_MAP.computeIfAbsent(type, this::createTypeHandler);
    }

    @SuppressWarnings("unchecked")
    public final <E extends Enum<E>> TypeHandler<?> createTypeHandler(Type type) {
        if (type instanceof Class<?> clazz) {
            if (isEnum(clazz)) {
                var enumClass = clazz.isAnonymousClass() ? clazz.getSuperclass() : clazz;
                return new EnumTypeHandler<>((Class<E>) enumClass);
            } else {
                //判断是否为可识别类型的子类
                for (var entry : TYPE_HANDLER_MAP.entrySet()) {
                    if (entry.getKey() instanceof Class<?> c && c.isAssignableFrom(clazz)) {
                        return entry.getValue();
                    }
                }
            }
        }
        return new ObjectTypeHandler(type);
    }

}
