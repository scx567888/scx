package cool.scx.log;

import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.helpers.NOPMDCAdapter;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;

public final class ScxSLF4JServiceProvider implements SLF4JServiceProvider {

    private static final String REQUESTED_API_VERSION = "2.0.99";

    private static final ScxLoggerFactory SCX_LOGGER_FACTORY = new ScxLoggerFactory();

    private static final IMarkerFactory markerFactory = new BasicMarkerFactory();

    private final MDCAdapter mdcAdapter = new NOPMDCAdapter();

    @Override
    public ILoggerFactory getLoggerFactory() {
        return SCX_LOGGER_FACTORY;
    }

    @Override
    public IMarkerFactory getMarkerFactory() {
        return markerFactory;
    }

    @Override
    public MDCAdapter getMDCAdapter() {
        return mdcAdapter;
    }

    @Override
    public String getRequestedApiVersion() {
        return REQUESTED_API_VERSION;
    }

    @Override
    public void initialize() {

    }

}
