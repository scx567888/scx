package cool.scx.object.parser;

import cool.scx.object.node.Node;

import java.io.File;
import java.io.IOException;

public interface NodeParser {

    Node parse(String text) throws NodeParseException;

    Node parse(File file) throws NodeParseException, IOException;

}
