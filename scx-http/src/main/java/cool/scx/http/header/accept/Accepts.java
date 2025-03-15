package cool.scx.http.header.accept;

/// Accepts
///
/// @author scx567888
/// @version 0.0.1
public interface Accepts extends Iterable<Accept> {

    static AcceptsWritable of(String acceptsStr) {
        return AcceptHelper.decodeAccepts(acceptsStr);
    }

    static AcceptsWritable of(Accepts oldAccepts) {
        return new AcceptsImpl(oldAccepts);
    }

    long size();

}
