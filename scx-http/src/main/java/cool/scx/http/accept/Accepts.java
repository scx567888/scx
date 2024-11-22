package cool.scx.http.accept;

public interface Accepts extends Iterable<Accept> {

    static AcceptsWritable of(String acceptsStr) {
        return AcceptHelper.decodeAccepts(acceptsStr);
    }

    static AcceptsWritable of(Accepts oldAccepts) {
        return new AcceptsImpl(oldAccepts);
    }

    long size();

}
