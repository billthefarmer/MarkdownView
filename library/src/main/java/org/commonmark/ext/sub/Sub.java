package org.commonmark.ext.sub;

import org.commonmark.node.CustomNode;
import org.commonmark.node.Delimited;

/**
 * A sub node containing text and other inline nodes as children.
 */
public class Sub extends CustomNode implements Delimited {

    private static final String DELIMITER = "~";

    @Override
    public String getOpeningDelimiter() {
        return DELIMITER;
    }

    @Override
    public String getClosingDelimiter() {
        return DELIMITER;
    }
}
