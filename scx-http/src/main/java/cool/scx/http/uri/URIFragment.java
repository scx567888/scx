package cool.scx.http.uri;

/**
 * URIFragment
 */
public interface URIFragment {

    static URIFragmentWritable of() {
        return new URIFragmentImpl();
    }

    static URIFragmentWritable of(String value) {
        return new URIFragmentImpl().value(value);
    }

    String value();

}
