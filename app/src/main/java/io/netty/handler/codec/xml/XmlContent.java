package io.netty.handler.codec.xml;

/* loaded from: classes4.dex */
public abstract class XmlContent {
    private final String data;

    /* JADX INFO: Access modifiers changed from: protected */
    public XmlContent(String data) {
        this.data = data;
    }

    public String data() {
        return this.data;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        XmlContent that = (XmlContent) o;
        if (this.data == null ? that.data == null : this.data.equals(that.data)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.data != null) {
            return this.data.hashCode();
        }
        return 0;
    }

    public String toString() {
        return "XmlContent{data='" + this.data + "'}";
    }
}
