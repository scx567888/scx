package cool.scx.sql;

import cool.scx.util.CaseUtils;
import cool.scx.util.ObjectUtils;
import cool.scx.util.RandomUtils;
import cool.scx.util.StringUtils;

import java.util.*;

/**
 * where 查询条件封装类
 *
 * @author scx567888
 * @version 1.2.0
 */
public final class Where {

    /**
     * 存储查询条件 key 为 fieldName ,采用 map 而不是 list 是为了保证重复添加的会直接覆盖
     */
    private final LinkedHashMap<String, WhereBody> whereBodyMap = new LinkedHashMap<>();

    /**
     * 所有 where 条件字符串 由 where 生成  类似 [ id = :id , age >= :age ]
     */
    private final List<String> whereColumns = new ArrayList<>();

    /**
     * 根据 where 条件对象生成的 map 防止 sql 注入
     */
    private final Map<String, Object> whereParamMap = new HashMap<>();

    /**
     * 自定义的查询语句
     */
    private String whereSQL;

    /**
     * <p>Constructor for Where.</p>
     */
    public Where() {

    }

    /**
     * <p>Constructor for Where.</p>
     *
     * @param whereSQL a {@link java.lang.String} object
     */
    public Where(String whereSQL) {
        whereSQL(whereSQL);
    }

    /**
     * <p>Constructor for Where.</p>
     *
     * @param fieldName a {@link java.lang.String} object
     * @param whereType a {@link cool.scx.sql.WhereType} object
     * @param value1    a {@link java.lang.Object} object
     * @param value2    a {@link java.lang.Object} object
     */
    public Where(String fieldName, WhereType whereType, Object value1, Object value2) {
        add(fieldName, whereType, value1, value2);
    }

    /**
     * <p>Constructor for Where.</p>
     *
     * @param fieldName a {@link java.lang.String} object
     * @param whereType a {@link cool.scx.sql.WhereType} object
     * @param value     a {@link java.lang.Object} object
     */
    public Where(String fieldName, WhereType whereType, Object value) {
        add(fieldName, whereType, value);
    }

    /**
     * <p>Constructor for Where.</p>
     *
     * @param fieldName a {@link java.lang.String} object
     * @param whereType a {@link cool.scx.sql.WhereType} object
     */
    public Where(String fieldName, WhereType whereType) {
        add(fieldName, whereType);
    }

    /**
     * 获取占位符
     *
     * @param name n
     * @return a {@link java.lang.String} object
     */
    private static String getPlaceholder(String name) {
        return name + "_" + RandomUtils.getRandomString(6, true);
    }

    /**
     * a
     *
     * @param value a
     * @return a
     */
    private static Object[] toArray(Object value) {
        var objectArray = new Object[0];
        if (value.getClass().isArray() || value instanceof List || value instanceof Set) {
            objectArray = ObjectUtils.convertValue(value, objectArray.getClass());
        } else if (value instanceof String) {
            objectArray = ((String) value).split(",");
        } else {
            objectArray = new Object[]{value};
        }
        return objectArray;
    }

    /**
     * 添加一个查询条件 (注意 : 此处添加的所有条件都会以 and 拼接 , 如需使用 or 请考虑使用 {@link #whereSQL(String)} })
     *
     * @param fieldName 字段名称 (注意不是数据库名称)
     * @param whereType where 类型
     * @param value1    参数1
     * @param value2    参数2
     * @return 本身 , 方便链式调用
     */
    public Where add(String fieldName, WhereType whereType, Object value1, Object value2) {
        if (fieldName == null) {
            throw new RuntimeException(" WhereType 类型 : " + whereType + " , 字段名称不能为空 !!!");
        }
        if (whereType.paramSize() != 2) {
            throw new RuntimeException(" WhereType 类型 : " + whereType + " , 参数数量必须为 " + whereType.paramSize());
        }
        if (value1 == null || value2 == null) {
            throw new RuntimeException(" WhereType 类型 : " + whereType + " , 参数列表不能为空 !!!");
        }
        return _add(fieldName, new WhereBody(fieldName, whereType, value1, value2));
    }

    /**
     * 添加一个查询条件 (注意 : 此处添加的所有条件都会以 and 拼接 , 如需使用 or 请考虑使用 {@link #whereSQL(String)} })
     *
     * @param fieldName 字段名称 (注意不是数据库名称)
     * @param whereType where 类型
     * @param value1    参数1
     * @return 本身 , 方便链式调用
     */
    public Where add(String fieldName, WhereType whereType, Object value1) {
        if (fieldName == null) {
            throw new RuntimeException(" WhereType 类型 : " + whereType + " , 字段名称不能为空 !!!");
        }
        if (whereType.paramSize() != 1) {
            throw new RuntimeException(" WhereType 类型 : " + whereType + " , 参数数量必须为 " + whereType.paramSize());
        }
        if (value1 == null) {
            throw new RuntimeException(" WhereType 类型 : " + whereType + " , 参数列表不能为空 !!!");
        }
        return _add(fieldName, new WhereBody(fieldName, whereType, value1, null));
    }

    /**
     * 添加一个查询条件 (注意 : 此处添加的所有条件都会以 and 拼接 , 如需使用 or 请考虑使用 {@link #whereSQL(String)} })
     *
     * @param fieldName 字段名称 (注意不是数据库名称)
     * @param whereType where 类型
     * @return 本身 , 方便链式调用
     */
    public Where add(String fieldName, WhereType whereType) {
        if (fieldName == null) {
            throw new RuntimeException(" WhereType 类型 : " + whereType + " , 字段名称不能为空 !!!");
        }
        if (whereType.paramSize() != 0) {
            throw new RuntimeException(" WhereType 类型 : " + whereType + " , 参数数量必须为 " + whereType.paramSize());
        }
        return _add(fieldName, new WhereBody(fieldName, whereType, null, null));
    }

    /**
     * 查询条件是否为空
     *
     * @return a boolean
     */
    public boolean isEmpty() {
        return whereBodyMap.size() == 0 && whereSQL == null;
    }

    /**
     * 为空
     *
     * @param fieldName 字段名称 (注意 : 不是数据库名称)
     * @return this 方便链式调用
     */
    public Where isNull(String fieldName) {
        return add(fieldName, WhereType.IS_NULL);
    }

    /**
     * 不为空
     *
     * @param fieldName 字段名称 (注意 : 不是数据库名称)
     * @return this 方便链式调用
     */
    public Where isNotNull(String fieldName) {
        return add(fieldName, WhereType.IS_NOT_NULL);
    }

    /**
     * 相等
     *
     * @param fieldName 字段名称 (注意 : 不是数据库名称)
     * @param value     比较值
     * @return this 方便链式调用
     */
    public Where equal(String fieldName, Object value) {
        return add(fieldName, WhereType.EQUAL, value);
    }

    /**
     * 不相等
     *
     * @param fieldName 字段名称 (注意 : 不是数据库名称)
     * @param value     比较值
     * @return this 方便链式调用
     */
    public Where notEqual(String fieldName, Object value) {
        return add(fieldName, WhereType.NOT_EQUAL, value);
    }

    /**
     * 大于
     *
     * @param fieldName 字段名称 (注意 : 不是数据库名称)
     * @param value     比较值
     * @return this 方便链式调用
     */
    public Where greaterThan(String fieldName, Object value) {
        return add(fieldName, WhereType.GREATER_THAN, value);
    }

    /**
     * 大于等于
     *
     * @param fieldName 字段名称 (注意 : 不是数据库名称)
     * @param value     比较值
     * @return this 方便链式调用
     */
    public Where greaterThanOrEqual(String fieldName, Object value) {
        return add(fieldName, WhereType.GREATER_THAN_OR_EQUAL, value);
    }

    /**
     * 小于
     *
     * @param fieldName 字段名称 (注意 : 不是数据库名称)
     * @param value     比较值
     * @return this 方便链式调用
     */
    public Where lessThan(String fieldName, Object value) {
        return add(fieldName, WhereType.LESS_THAN, value);
    }

    /**
     * 小于等于
     *
     * @param fieldName 字段名称 (注意 : 不是数据库名称)
     * @param value     比较值
     * @return this 方便链式调用
     */
    public Where lessThanOrEqual(String fieldName, Object value) {
        return add(fieldName, WhereType.LESS_THAN_OR_EQUAL, value);
    }

    /**
     * 两者之间
     *
     * @param fieldName 字段名称 (注意 : 不是数据库名称)
     * @param value1    比较值1
     * @param value2    比较值2
     * @return this 方便链式调用
     */
    public Where between(String fieldName, Object value1, Object value2) {
        return add(fieldName, WhereType.BETWEEN, value1, value2);
    }

    /**
     * 不处于两者之间
     *
     * @param fieldName 字段名称 (注意 : 不是数据库名称)
     * @param value1    比较值1
     * @param value2    比较值2
     * @return this 方便链式调用
     */
    public Where notBetween(String fieldName, Object value1, Object value2) {
        return add(fieldName, WhereType.NOT_BETWEEN, value1, value2);
    }

    /**
     * like : 根据 SQL 表达式进行判断
     *
     * @param fieldName 字段名称 (注意 : 不是数据库名称)
     * @param value     SQL 表达式
     * @return this 方便链式调用
     */
    public Where likeRegex(String fieldName, String value) {
        return add(fieldName, WhereType.LIKE_REGEX, value);
    }

    /**
     * not like : 根据 SQL 表达式进行判断
     *
     * @param fieldName 字段名称 (注意 : 不是数据库名称)
     * @param value     SQL 表达式
     * @return this 方便链式调用
     */
    public Where notLikeRegex(String fieldName, String value) {
        return add(fieldName, WhereType.NOT_LIKE_REGEX, value);
    }

    /**
     * like : 默认会在首尾添加 %
     *
     * @param fieldName 字段名称 (注意 : 不是数据库名称)
     * @param value     参数 默认会在首尾添加 %
     * @return this 方便链式调用
     */
    public Where like(String fieldName, Object value) {
        return add(fieldName, WhereType.LIKE, value);
    }

    /**
     * not like : 默认会在首尾添加 %
     *
     * @param fieldName 字段名称 (注意 : 不是数据库名称)
     * @param value     默认会在首尾添加 %
     * @return this 方便链式调用
     */
    public Where notLike(String fieldName, Object value) {
        return add(fieldName, WhereType.NOT_LIKE, value);
    }

    /**
     * 包含  : 一般用于 JSON 格式字段 区别于 in
     *
     * @param fieldName 字段名称 (注意 : 不是数据库名称)
     * @param value     比较值
     * @return this 方便链式调用
     */
    public Where jsonContains(String fieldName, Object value) {
        return add(fieldName, WhereType.JSON_CONTAINS, value);
    }

    /**
     * 在其中
     *
     * @param fieldName 字段名称 (注意 : 不是数据库名称)
     * @param value     比较值
     * @return this 方便链式调用
     */
    public Where in(String fieldName, Object value) {
        return add(fieldName, WhereType.IN, value);
    }

    /**
     * 不在其中
     *
     * @param fieldName 字段名称 (注意 : 不是数据库名称)
     * @param value     比较值
     * @return this 方便链式调用
     */
    public Where notIn(String fieldName, Object value) {
        return add(fieldName, WhereType.NOT_IN, value);
    }

    /**
     * 获取 where 语句
     *
     * @return w
     */
    String[] getWhereClauses() {
        var whereClauses = new ArrayList<>(this.whereColumns);
        if (StringUtils.isNotBlank(this.whereSQL)) {
            whereClauses.add(this.whereSQL);
        }
        return whereClauses.toArray(new String[0]);
    }

    /**
     * <p>Getter for the field <code>whereParamMap</code>.</p>
     *
     * @return a {@link java.util.Map} object
     */
    public Map<String, Object> getWhereParamMap() {
        return whereParamMap;
    }

    /**
     * <p>Getter for the field <code>whereSQL</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String whereSQL() {
        return whereSQL;
    }

    /**
     * 设置 whereSQL 适用于 复杂查询的自定义 where 子句<br>
     * 在最终 sql 中会拼接到 where 子句的最后<br>
     * 注意 :  除特殊语法外不需要手动在头部添加 AND
     *
     * @param whereSQL sql 语句
     * @return 本身 , 方便链式调用
     */
    public Where whereSQL(String whereSQL) {
        this.whereSQL = whereSQL;
        return this;
    }

    /**
     * aa
     *
     * @param f a
     * @param w a
     * @return a
     */
    private Where _add(String f, WhereBody w) {
        whereBodyMap.put(f, w);
        var columnName = CaseUtils.toSnake(w.fieldName());
        var keyWord = w.whereType().keyWord();
        var fieldName = w.fieldName();
        var value1 = w.value1();
        var value2 = w.value2();

        switch (w.whereType()) {
            case IS_NULL, IS_NOT_NULL -> {
                var str = columnName + " " + keyWord;
                whereColumns.add(str);
            }
            case EQUAL, NOT_EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL, LIKE_REGEX, NOT_LIKE_REGEX -> {
                var placeholder = getPlaceholder(fieldName);
                var str = columnName + " " + keyWord + " :" + placeholder;
                whereParamMap.put(placeholder, value1);
                whereColumns.add(str);
            }
            case JSON_CONTAINS -> {
                var jsonContainsParams = toArray(value1);
                var placeholder = getPlaceholder(fieldName);
                var str = keyWord + " ( " + columnName + ", :" + placeholder + " )";
                whereParamMap.put(placeholder, jsonContainsParams);
                whereColumns.add(str);
            }
            case LIKE, NOT_LIKE -> {
                var placeholder = getPlaceholder(fieldName);
                var str = columnName + " " + keyWord + " CONCAT('%',:" + placeholder + ",'%')";
                whereParamMap.put(placeholder, value1);
                whereColumns.add(str);
            }
            case IN, NOT_IN -> {
                var inParams = toArray(value1);
                var sList = new String[inParams.length];
                for (int i = 0; i < inParams.length; i++) {
                    var placeholder = getPlaceholder(fieldName);
                    sList[i] = ":" + placeholder;
                    whereParamMap.put(placeholder, inParams[i]);
                }
                var str = columnName + " " + keyWord + " ( " + String.join(",", sList) + " )";
                whereColumns.add(str);
            }
            case BETWEEN, NOT_BETWEEN -> {
                var placeholder1 = getPlaceholder(fieldName);
                var placeholder2 = getPlaceholder(fieldName);
                var str = columnName + " " + keyWord + " :" + placeholder1 + " AND :" + placeholder2;
                whereParamMap.put(placeholder1, value1);
                whereParamMap.put(placeholder2, value2);
                whereColumns.add(str);
            }
        }
        return this;
    }

}
