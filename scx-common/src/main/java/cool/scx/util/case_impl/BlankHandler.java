package cool.scx.util.case_impl;

/**
 * <p>BlankHandler class.</p>
 *
 * @author scx567888
 * @version 0.0.8
 */
final class BlankHandler implements CaseTypeHandler {

    /**
     * Constant <code>EMPTY_ARRAY</code>
     */
    private static final String[] EMPTY_ARRAY = new String[]{};

    /**
     * Constant <code>EMPTY_STRING=""</code>
     */
    private static final String EMPTY_STRING = "";

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getSourceStrings(String s) {
        return EMPTY_ARRAY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(String[] s) {
        return EMPTY_STRING;
    }

}
