package io.netty.handler.codec.dns;

import io.netty.buffer.ByteBuf;

/* loaded from: classes4.dex */
public class DefaultDnsRecordDecoder implements DnsRecordDecoder {
    static final String ROOT = ".";

    @Override // io.netty.handler.codec.dns.DnsRecordDecoder
    public final DnsQuestion decodeQuestion(ByteBuf in) throws Exception {
        String name = decodeName(in);
        DnsRecordType type = DnsRecordType.valueOf(in.readUnsignedShort());
        int qClass = in.readUnsignedShort();
        return new DefaultDnsQuestion(name, type, qClass);
    }

    @Override // io.netty.handler.codec.dns.DnsRecordDecoder
    public final <T extends DnsRecord> T decodeRecord(ByteBuf byteBuf) throws Exception {
        int readerIndex = byteBuf.readerIndex();
        String decodeName = decodeName(byteBuf);
        int writerIndex = byteBuf.writerIndex();
        if (writerIndex - byteBuf.readerIndex() < 10) {
            byteBuf.readerIndex(readerIndex);
            return null;
        }
        DnsRecordType valueOf = DnsRecordType.valueOf(byteBuf.readUnsignedShort());
        int readUnsignedShort = byteBuf.readUnsignedShort();
        long readUnsignedInt = byteBuf.readUnsignedInt();
        int readUnsignedShort2 = byteBuf.readUnsignedShort();
        int readerIndex2 = byteBuf.readerIndex();
        if (writerIndex - readerIndex2 < readUnsignedShort2) {
            byteBuf.readerIndex(readerIndex);
            return null;
        }
        T t = (T) decodeRecord(decodeName, valueOf, readUnsignedShort, readUnsignedInt, byteBuf, readerIndex2, readUnsignedShort2);
        byteBuf.readerIndex(readerIndex2 + readUnsignedShort2);
        return t;
    }

    protected DnsRecord decodeRecord(String name, DnsRecordType type, int dnsClass, long timeToLive, ByteBuf in, int offset, int length) throws Exception {
        if (type != DnsRecordType.PTR) {
            if (type == DnsRecordType.CNAME || type == DnsRecordType.NS) {
                return new DefaultDnsRawRecord(name, type, dnsClass, timeToLive, DnsCodecUtil.decompressDomainName(in.duplicate().setIndex(offset, offset + length)));
            }
            return new DefaultDnsRawRecord(name, type, dnsClass, timeToLive, in.retainedDuplicate().setIndex(offset, offset + length));
        }
        return new DefaultDnsPtrRecord(name, dnsClass, timeToLive, decodeName0(in.duplicate().setIndex(offset, offset + length)));
    }

    protected String decodeName0(ByteBuf in) {
        return decodeName(in);
    }

    public static String decodeName(ByteBuf in) {
        return DnsCodecUtil.decodeDomainName(in);
    }
}
