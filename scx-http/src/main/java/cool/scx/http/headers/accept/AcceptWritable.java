package cool.scx.http.headers.accept;

/// AcceptsWritable
///
/// @author scx567888
/// @version 0.0.1
public interface AcceptWritable extends Accept {

    AcceptWritable add(MediaRange mediaRange);

    AcceptWritable remove(MediaRange mediaRange);

}
