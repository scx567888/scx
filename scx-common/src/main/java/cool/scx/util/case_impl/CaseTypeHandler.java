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
     * @param s a {@link String} object
     * @return an array of {@link String} objects
     */
    String[] getSourceStrings(String s);

    /**
     * <p>getString.</p>
     *
     * @param s an array of {@link String} objects
     * @return a {@link String} object
     */
    String getString(String[] s);

}
