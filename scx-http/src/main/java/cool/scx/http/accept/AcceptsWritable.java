package cool.scx.http.accept;

public interface AcceptsWritable extends Accepts {

    AcceptsWritable add(Accept accept);

    AcceptsWritable remove(Accept accept);

}
