package io.netty.handler.codec.xml;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes4.dex */
public class XmlElementStart extends XmlElement {
    private final List<XmlAttribute> attributes;

    public XmlElementStart(String name, String namespace, String prefix) {
        super(name, namespace, prefix);
        this.attributes = new ArrayList();
    }

    public List<XmlAttribute> attributes() {
        return this.attributes;
    }

    @Override // io.netty.handler.codec.xml.XmlElement
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass() || !super.equals(o)) {
            return false;
        }
        XmlElementStart that = (XmlElementStart) o;
        return this.attributes.equals(that.attributes);
    }

    @Override // io.netty.handler.codec.xml.XmlElement
    public int hashCode() {
        int result = super.hashCode();
        return (result * 31) + this.attributes.hashCode();
    }

    @Override // io.netty.handler.codec.xml.XmlElement
    public String toString() {
        return "XmlElementStart{attributes=" + this.attributes + super.toString() + "} ";
    }
}
