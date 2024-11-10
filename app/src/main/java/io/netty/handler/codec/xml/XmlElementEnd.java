package io.netty.handler.codec.xml;

/* loaded from: classes4.dex */
public class XmlElementEnd extends XmlElement {
    public XmlElementEnd(String name, String namespace, String prefix) {
        super(name, namespace, prefix);
    }

    @Override // io.netty.handler.codec.xml.XmlElement
    public String toString() {
        return "XmlElementStart{" + super.toString() + "} ";
    }
}
