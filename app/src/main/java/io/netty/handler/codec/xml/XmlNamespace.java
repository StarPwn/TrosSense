package io.netty.handler.codec.xml;

/* loaded from: classes4.dex */
public class XmlNamespace {
    private final String prefix;
    private final String uri;

    public XmlNamespace(String prefix, String uri) {
        this.prefix = prefix;
        this.uri = uri;
    }

    public String prefix() {
        return this.prefix;
    }

    public String uri() {
        return this.uri;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        XmlNamespace that = (XmlNamespace) o;
        if (this.prefix == null ? that.prefix != null : !this.prefix.equals(that.prefix)) {
            return false;
        }
        if (this.uri == null ? that.uri == null : this.uri.equals(that.uri)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.prefix != null ? this.prefix.hashCode() : 0;
        return (result * 31) + (this.uri != null ? this.uri.hashCode() : 0);
    }

    public String toString() {
        return "XmlNamespace{prefix='" + this.prefix + "', uri='" + this.uri + "'}";
    }
}
