package cool.scx.util.case_impl;

/**
 * <p>CaseTypeHandler interface.</p>
 *
 * @author scx567888
 * @version 0.0.8
 */
interface CaseTypeHandler {

    /**
     * <p>getSourceStrings.</p>
     *
     * @param s a {@link java.lang.String} object
     * @return an array of {@link java.lang.String} objects
     */
    String[] getSourceStrings(String s);

    /**
     * <p>getString.</p>
     *
     * @param s an array of {@link java.lang.String} objects
     * @return a {@link java.lang.String} object
     */
    String getString(String[] s);

}
