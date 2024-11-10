package io.netty.handler.codec.xml;

/* loaded from: classes4.dex */
public class XmlDocumentStart {
    private final String encoding;
    private final String encodingScheme;
    private final boolean standalone;
    private final String version;

    public XmlDocumentStart(String encoding, String version, boolean standalone, String encodingScheme) {
        this.encoding = encoding;
        this.version = version;
        this.standalone = standalone;
        this.encodingScheme = encodingScheme;
    }

    public String encoding() {
        return this.encoding;
    }

    public String version() {
        return this.version;
    }

    public boolean standalone() {
        return this.standalone;
    }

    public String encodingScheme() {
        return this.encodingScheme;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        XmlDocumentStart that = (XmlDocumentStart) o;
        if (this.standalone != that.standalone) {
            return false;
        }
        if (this.encoding == null ? that.encoding != null : !this.encoding.equals(that.encoding)) {
            return false;
        }
        if (this.encodingScheme == null ? that.encodingScheme != null : !this.encodingScheme.equals(that.encodingScheme)) {
            return false;
        }
        if (this.version == null ? that.version == null : this.version.equals(that.version)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return ((((((this.encoding != null ? this.encoding.hashCode() : 0) * 31) + (this.version != null ? this.version.hashCode() : 0)) * 31) + (this.standalone ? 1 : 0)) * 31) + (this.encodingScheme != null ? this.encodingScheme.hashCode() : 0);
    }

    public String toString() {
        return "XmlDocumentStart{encoding='" + this.encoding + "', version='" + this.version + "', standalone=" + this.standalone + ", encodingScheme='" + this.encodingScheme + "'}";
    }
}
