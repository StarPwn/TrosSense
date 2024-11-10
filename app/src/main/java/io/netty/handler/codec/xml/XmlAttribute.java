package io.netty.handler.codec.xml;

/* loaded from: classes4.dex */
public class XmlAttribute {
    private final String name;
    private final String namespace;
    private final String prefix;
    private final String type;
    private final String value;

    public XmlAttribute(String type, String name, String prefix, String namespace, String value) {
        this.type = type;
        this.name = name;
        this.prefix = prefix;
        this.namespace = namespace;
        this.value = value;
    }

    public String type() {
        return this.type;
    }

    public String name() {
        return this.name;
    }

    public String prefix() {
        return this.prefix;
    }

    public String namespace() {
        return this.namespace;
    }

    public String value() {
        return this.value;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        XmlAttribute that = (XmlAttribute) o;
        if (!this.name.equals(that.name)) {
            return false;
        }
        if (this.namespace == null ? that.namespace != null : !this.namespace.equals(that.namespace)) {
            return false;
        }
        if (this.prefix == null ? that.prefix != null : !this.prefix.equals(that.prefix)) {
            return false;
        }
        if (this.type == null ? that.type != null : !this.type.equals(that.type)) {
            return false;
        }
        if (this.value == null ? that.value == null : this.value.equals(that.value)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.type != null ? this.type.hashCode() : 0;
        return (((((((result * 31) + this.name.hashCode()) * 31) + (this.prefix != null ? this.prefix.hashCode() : 0)) * 31) + (this.namespace != null ? this.namespace.hashCode() : 0)) * 31) + (this.value != null ? this.value.hashCode() : 0);
    }

    public String toString() {
        return "XmlAttribute{type='" + this.type + "', name='" + this.name + "', prefix='" + this.prefix + "', namespace='" + this.namespace + "', value='" + this.value + "'}";
    }
}
