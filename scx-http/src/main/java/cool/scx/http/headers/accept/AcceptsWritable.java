package cool.scx.http.headers.accept;

/// AcceptsWritable
///
/// @author scx567888
/// @version 0.0.1
public interface AcceptsWritable extends Accepts {

    AcceptsWritable add(Accept accept);

    AcceptsWritable remove(Accept accept);

}
