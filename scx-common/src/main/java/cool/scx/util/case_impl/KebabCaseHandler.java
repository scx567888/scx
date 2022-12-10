package cool.scx.util.case_impl;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * <p>KebabCaseHandler class.</p>
 *
 * @author scx567888
 * @version 0.0.8
 */
final class KebabCaseHandler implements CaseTypeHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getSourceStrings(String s) {
        return s.split("-");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(String[] s) {
        return Arrays.stream(s).map(String::toLowerCase).collect(Collectors.joining("-"));
    }

}
