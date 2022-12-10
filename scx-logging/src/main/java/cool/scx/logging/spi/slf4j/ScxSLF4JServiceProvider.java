package cool.scx.logging.spi.slf4j;

import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.helpers.NOPMDCAdapter;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;

/**
 * a
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ScxSLF4JServiceProvider implements SLF4JServiceProvider {

    /**
     * Constant <code>REQUESTED_API_VERSION="2.0.99"</code>
     */
    private static final String REQUESTED_API_VERSION = "2.0.99";

    /**
     * Constant <code>SCX_LOGGER_FACTORY</code>
     */
    private static final ScxSLF4JLoggerFactory SCX_LOGGER_FACTORY = new ScxSLF4JLoggerFactory();

    /**
     * Constant <code>markerFactory</code>
     */
    private static final IMarkerFactory markerFactory = new BasicMarkerFactory();

    /**
     * a
     */
    private final MDCAdapter mdcAdapter = new NOPMDCAdapter();

    /**
     * {@inheritDoc}
     */
    @Override
    public ILoggerFactory getLoggerFactory() {
        return SCX_LOGGER_FACTORY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMarkerFactory getMarkerFactory() {
        return markerFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MDCAdapter getMDCAdapter() {
        return mdcAdapter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRequestedApiVersion() {
        return REQUESTED_API_VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {

    }

}
