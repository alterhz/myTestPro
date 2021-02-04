package org.game.core;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class FromPoint {
    private final String node;
    private final String port;

    public FromPoint(String node, String port) {
        this.node = node;
        this.port = port;
    }

    public String getNode() {
        return node;
    }

    public String getPort() {
        return port;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        FromPoint fromPoint = (FromPoint) o;

        return new EqualsBuilder().append(node, fromPoint.node)
                .append(port, fromPoint.port)
                .isEquals();
    }

    @Override public int hashCode() {
        return new HashCodeBuilder(17, 37).append(node).append(port).toHashCode();
    }

    @Override public String toString() {
        return new ToStringBuilder(this).append("node", node).append("port", port).toString();
    }
}
