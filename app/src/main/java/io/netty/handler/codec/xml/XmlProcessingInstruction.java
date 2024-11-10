package io.netty.handler.codec.xml;

/* loaded from: classes4.dex */
public class XmlProcessingInstruction {
    private final String data;
    private final String target;

    public XmlProcessingInstruction(String data, String target) {
        this.data = data;
        this.target = target;
    }

    public String data() {
        return this.data;
    }

    public String target() {
        return this.target;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        XmlProcessingInstruction that = (XmlProcessingInstruction) o;
        if (this.data == null ? that.data != null : !this.data.equals(that.data)) {
            return false;
        }
        if (this.target == null ? that.target == null : this.target.equals(that.target)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.data != null ? this.data.hashCode() : 0;
        return (result * 31) + (this.target != null ? this.target.hashCode() : 0);
    }

    public String toString() {
        return "XmlProcessingInstruction{data='" + this.data + "', target='" + this.target + "'}";
    }
}
