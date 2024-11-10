package io.netty.handler.codec.xml;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes4.dex */
public abstract class XmlElement {
    private final String name;
    private final String namespace;
    private final List<XmlNamespace> namespaces = new ArrayList();
    private final String prefix;

    /* JADX INFO: Access modifiers changed from: protected */
    public XmlElement(String name, String namespace, String prefix) {
        this.name = name;
        this.namespace = namespace;
        this.prefix = prefix;
    }

    public String name() {
        return this.name;
    }

    public String namespace() {
        return this.namespace;
    }

    public String prefix() {
        return this.prefix;
    }

    public List<XmlNamespace> namespaces() {
        return this.namespaces;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        XmlElement that = (XmlElement) o;
        if (!this.name.equals(that.name)) {
            return false;
        }
        if (this.namespace == null ? that.namespace != null : !this.namespace.equals(that.namespace)) {
            return false;
        }
        if (!this.namespaces.equals(that.namespaces)) {
            return false;
        }
        if (this.prefix == null ? that.prefix == null : this.prefix.equals(that.prefix)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.name.hashCode();
        return (((((result * 31) + (this.namespace != null ? this.namespace.hashCode() : 0)) * 31) + (this.prefix != null ? this.prefix.hashCode() : 0)) * 31) + this.namespaces.hashCode();
    }

    public String toString() {
        return ", name='" + this.name + "', namespace='" + this.namespace + "', prefix='" + this.prefix + "', namespaces=" + this.namespaces;
    }
}
