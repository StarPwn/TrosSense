package io.netty.handler.codec.dns;

import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import org.msgpack.core.MessagePack;

/* loaded from: classes4.dex */
public class DefaultDnsRecordEncoder implements DnsRecordEncoder {
    private static final int PREFIX_MASK = 7;
    private static final Class<?>[] SUPPORTED_MESSAGES = {DnsQuestion.class, DnsPtrRecord.class, DnsOptEcsRecord.class, DnsOptPseudoRecord.class, DnsRawRecord.class};

    @Override // io.netty.handler.codec.dns.DnsRecordEncoder
    public final void encodeQuestion(DnsQuestion question, ByteBuf out) throws Exception {
        encodeName(question.name(), out);
        out.writeShort(question.type().intValue());
        out.writeShort(question.dnsClass());
    }

    @Override // io.netty.handler.codec.dns.DnsRecordEncoder
    public void encodeRecord(DnsRecord record, ByteBuf out) throws Exception {
        if (record instanceof DnsQuestion) {
            encodeQuestion((DnsQuestion) record, out);
            return;
        }
        if (record instanceof DnsPtrRecord) {
            encodePtrRecord((DnsPtrRecord) record, out);
            return;
        }
        if (record instanceof DnsOptEcsRecord) {
            encodeOptEcsRecord((DnsOptEcsRecord) record, out);
        } else if (record instanceof DnsOptPseudoRecord) {
            encodeOptPseudoRecord((DnsOptPseudoRecord) record, out);
        } else {
            if (record instanceof DnsRawRecord) {
                encodeRawRecord((DnsRawRecord) record, out);
                return;
            }
            throw new UnsupportedMessageTypeException(record, SUPPORTED_MESSAGES);
        }
    }

    private void encodeRecord0(DnsRecord record, ByteBuf out) throws Exception {
        encodeName(record.name(), out);
        out.writeShort(record.type().intValue());
        out.writeShort(record.dnsClass());
        out.writeInt((int) record.timeToLive());
    }

    private void encodePtrRecord(DnsPtrRecord record, ByteBuf out) throws Exception {
        encodeRecord0(record, out);
        encodeName(record.hostname(), out);
    }

    private void encodeOptPseudoRecord(DnsOptPseudoRecord record, ByteBuf out) throws Exception {
        encodeRecord0(record, out);
        out.writeShort(0);
    }

    private void encodeOptEcsRecord(DnsOptEcsRecord record, ByteBuf out) throws Exception {
        encodeRecord0(record, out);
        int sourcePrefixLength = record.sourcePrefixLength();
        int scopePrefixLength = record.scopePrefixLength();
        int lowOrderBitsToPreserve = sourcePrefixLength & 7;
        byte[] bytes = record.address();
        int addressBits = bytes.length << 3;
        if (addressBits < sourcePrefixLength || sourcePrefixLength < 0) {
            throw new IllegalArgumentException(sourcePrefixLength + ": " + sourcePrefixLength + " (expected: 0 >= " + addressBits + ')');
        }
        short addressNumber = (short) (bytes.length == 4 ? InternetProtocolFamily.IPv4 : InternetProtocolFamily.IPv6).addressNumber();
        int payloadLength = calculateEcsAddressLength(sourcePrefixLength, lowOrderBitsToPreserve);
        int fullPayloadLength = payloadLength + 8;
        out.writeShort(fullPayloadLength);
        out.writeShort(8);
        out.writeShort(fullPayloadLength - 4);
        out.writeShort(addressNumber);
        out.writeByte(sourcePrefixLength);
        out.writeByte(scopePrefixLength);
        if (lowOrderBitsToPreserve > 0) {
            int bytesLength = payloadLength - 1;
            out.writeBytes(bytes, 0, bytesLength);
            out.writeByte(padWithZeros(bytes[bytesLength], lowOrderBitsToPreserve));
            return;
        }
        out.writeBytes(bytes, 0, payloadLength);
    }

    static int calculateEcsAddressLength(int sourcePrefixLength, int lowOrderBitsToPreserve) {
        return (sourcePrefixLength >>> 3) + (lowOrderBitsToPreserve != 0 ? 1 : 0);
    }

    private void encodeRawRecord(DnsRawRecord record, ByteBuf out) throws Exception {
        encodeRecord0(record, out);
        ByteBuf content = record.content();
        int contentLen = content.readableBytes();
        out.writeShort(contentLen);
        out.writeBytes(content, content.readerIndex(), contentLen);
    }

    protected void encodeName(String name, ByteBuf buf) throws Exception {
        DnsCodecUtil.encodeDomainName(name, buf);
    }

    private static byte padWithZeros(byte b, int lowOrderBitsToPreserve) {
        switch (lowOrderBitsToPreserve) {
            case 0:
                return (byte) 0;
            case 1:
                return (byte) (b & 128);
            case 2:
                return (byte) (b & MessagePack.Code.NIL);
            case 3:
                return (byte) (b & MessagePack.Code.NEGFIXINT_PREFIX);
            case 4:
                return (byte) (b & 240);
            case 5:
                return (byte) (b & 248);
            case 6:
                return (byte) (b & 252);
            case 7:
                return (byte) (b & 254);
            case 8:
                return b;
            default:
                throw new IllegalArgumentException("lowOrderBitsToPreserve: " + lowOrderBitsToPreserve);
        }
    }
}
