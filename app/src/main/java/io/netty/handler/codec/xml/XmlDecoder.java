package io.netty.handler.codec.xml;

import com.fasterxml.aalto.AsyncByteArrayFeeder;
import com.fasterxml.aalto.AsyncXMLInputFactory;
import com.fasterxml.aalto.AsyncXMLStreamReader;
import com.fasterxml.aalto.stax.InputFactoryImpl;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;
import javax.xml.stream.XMLStreamException;

/* loaded from: classes4.dex */
public class XmlDecoder extends ByteToMessageDecoder {
    private static final AsyncXMLInputFactory XML_INPUT_FACTORY = new InputFactoryImpl();
    private static final XmlDocumentEnd XML_DOCUMENT_END = XmlDocumentEnd.INSTANCE;
    private final AsyncXMLStreamReader<AsyncByteArrayFeeder> streamReader = XML_INPUT_FACTORY.createAsyncForByteArray();
    private final AsyncByteArrayFeeder streamFeeder = this.streamReader.getInputFeeder();

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] buffer = new byte[in.readableBytes()];
        in.readBytes(buffer);
        try {
            this.streamFeeder.feedInput(buffer, 0, buffer.length);
            while (!this.streamFeeder.needMoreInput()) {
                int type = this.streamReader.next();
                switch (type) {
                    case 1:
                        XmlElementStart elementStart = new XmlElementStart(this.streamReader.getLocalName(), this.streamReader.getName().getNamespaceURI(), this.streamReader.getPrefix());
                        for (int x = 0; x < this.streamReader.getAttributeCount(); x++) {
                            XmlAttribute attribute = new XmlAttribute(this.streamReader.getAttributeType(x), this.streamReader.getAttributeLocalName(x), this.streamReader.getAttributePrefix(x), this.streamReader.getAttributeNamespace(x), this.streamReader.getAttributeValue(x));
                            elementStart.attributes().add(attribute);
                        }
                        for (int x2 = 0; x2 < this.streamReader.getNamespaceCount(); x2++) {
                            XmlNamespace namespace = new XmlNamespace(this.streamReader.getNamespacePrefix(x2), this.streamReader.getNamespaceURI(x2));
                            elementStart.namespaces().add(namespace);
                        }
                        out.add(elementStart);
                        break;
                    case 2:
                        XmlElementEnd elementEnd = new XmlElementEnd(this.streamReader.getLocalName(), this.streamReader.getName().getNamespaceURI(), this.streamReader.getPrefix());
                        for (int x3 = 0; x3 < this.streamReader.getNamespaceCount(); x3++) {
                            XmlNamespace namespace2 = new XmlNamespace(this.streamReader.getNamespacePrefix(x3), this.streamReader.getNamespaceURI(x3));
                            elementEnd.namespaces().add(namespace2);
                        }
                        out.add(elementEnd);
                        break;
                    case 3:
                        out.add(new XmlProcessingInstruction(this.streamReader.getPIData(), this.streamReader.getPITarget()));
                        break;
                    case 4:
                        out.add(new XmlCharacters(this.streamReader.getText()));
                        break;
                    case 5:
                        out.add(new XmlComment(this.streamReader.getText()));
                        break;
                    case 6:
                        out.add(new XmlSpace(this.streamReader.getText()));
                        break;
                    case 7:
                        out.add(new XmlDocumentStart(this.streamReader.getEncoding(), this.streamReader.getVersion(), this.streamReader.isStandalone(), this.streamReader.getCharacterEncodingScheme()));
                        break;
                    case 8:
                        out.add(XML_DOCUMENT_END);
                        break;
                    case 9:
                        out.add(new XmlEntityReference(this.streamReader.getLocalName(), this.streamReader.getText()));
                        break;
                    case 11:
                        out.add(new XmlDTD(this.streamReader.getText()));
                        break;
                    case 12:
                        out.add(new XmlCdata(this.streamReader.getText()));
                        break;
                }
            }
        } catch (XMLStreamException exception) {
            in.skipBytes(in.readableBytes());
            throw exception;
        }
    }
}
