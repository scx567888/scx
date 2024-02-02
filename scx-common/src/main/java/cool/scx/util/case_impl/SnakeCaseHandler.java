package cool.scx.util.case_impl;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * <p>SnakeCaseHandler class.</p>
 *
 * @author scx567888
 * @version 0.0.8
 */
final class SnakeCaseHandler implements CaseTypeHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getSourceStrings(String s) {
        return s.split("_");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(String[] s) {
        return Arrays.stream(s).map(String::toLowerCase).collect(Collectors.joining("_"));
    }

}
