package cool.scx.http.accept;

public interface Accepts extends Iterable<Accept> {

    static AcceptsWritable of(String accepts) {
        return AcceptHelper.decodeAccepts(accepts);
    }

    long size();

}
