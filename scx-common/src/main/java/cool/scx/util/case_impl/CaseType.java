package cool.scx.util.case_impl;

/**
 * <p>CaseType class.</p>
 *
 * @author scx567888
 * @version 0.0.8
 */
public enum CaseType implements CaseTypeHandler {

    /**
     * 驼峰命名法 getNameByAge
     */
    CAMEL_CASE(new CamelCaseHandler()),

    /**
     * 匈牙利命名法 GetNameByAge
     */
    PASCAL_CASE(new PascalCaseHandler()),

    /**
     * 短横线命名法 get-name-by-age
     */
    KEBAB_CASE(new KebabCaseHandler()),

    /**
     * 蛇形命名法 get_name_by_age
     */
    SNAKE_CASE(new SnakeCaseHandler()),

    /**
     * 空白字符串
     */
    BLANK(new BlankHandler());

    private final CaseTypeHandler handler;

    CaseType(CaseTypeHandler handler) {
        this.handler = handler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getSourceStrings(String o) {
        return handler.getSourceStrings(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(String[] s) {
        return handler.getString(s);
    }

}
