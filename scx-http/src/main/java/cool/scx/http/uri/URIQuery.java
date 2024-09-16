package cool.scx.http.uri;

import cool.scx.http.Parameters;

import static cool.scx.http.uri.URIHelper.decodeQuery;

/**
 * URIQuery
 */
public interface URIQuery extends Parameters {

    static URIQueryWritable of() {
        return new URIQueryImpl();
    }

    static URIQueryWritable of(String queryString) {
        return decodeQuery(queryString);
    }

}
