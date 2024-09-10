package cool.scx.http_server;

public interface ScxHttpPathQueryWritable extends ScxHttpPathQuery {

    ScxHttpPathQueryImpl set(String name, String value);

    ScxHttpPathQueryImpl add(String name, String value);

}
