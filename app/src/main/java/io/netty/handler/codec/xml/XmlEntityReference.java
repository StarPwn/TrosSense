package io.netty.handler.codec.xml;

/* loaded from: classes4.dex */
public class XmlEntityReference {
    private final String name;
    private final String text;

    public XmlEntityReference(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public String name() {
        return this.name;
    }

    public String text() {
        return this.text;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        XmlEntityReference that = (XmlEntityReference) o;
        if (this.name == null ? that.name != null : !this.name.equals(that.name)) {
            return false;
        }
        if (this.text != null) {
            return this.text.equals(that.text);
        }
        if (that.text == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.name != null ? this.name.hashCode() : 0;
        return (result * 31) + (this.text != null ? this.text.hashCode() : 0);
    }

    public String toString() {
        return "XmlEntityReference{name='" + this.name + "', text='" + this.text + "'}";
    }
}
