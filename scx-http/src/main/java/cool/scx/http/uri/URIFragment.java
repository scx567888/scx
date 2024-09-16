package cool.scx.http.uri;

public interface URIFragment {

    static URIFragment of() {
        return new URIFragmentImpl();
    }

    static URIFragment of(String value) {
        return new URIFragmentImpl().value(value);
    }

    String value();

}
