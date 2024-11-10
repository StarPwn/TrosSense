package io.netty.handler.codec.dns;

import io.netty.buffer.ByteBuf;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
final class DnsQueryEncoder {
    private final DnsRecordEncoder recordEncoder;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DnsQueryEncoder(DnsRecordEncoder recordEncoder) {
        this.recordEncoder = (DnsRecordEncoder) ObjectUtil.checkNotNull(recordEncoder, "recordEncoder");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void encode(DnsQuery query, ByteBuf out) throws Exception {
        encodeHeader(query, out);
        encodeQuestions(query, out);
        encodeRecords(query, DnsSection.ADDITIONAL, out);
    }

    private static void encodeHeader(DnsQuery query, ByteBuf buf) {
        buf.writeShort(query.id());
        int flags = 0 | ((query.opCode().byteValue() & 255) << 14);
        if (query.isRecursionDesired()) {
            flags |= 256;
        }
        buf.writeShort(flags);
        buf.writeShort(query.count(DnsSection.QUESTION));
        buf.writeShort(0);
        buf.writeShort(0);
        buf.writeShort(query.count(DnsSection.ADDITIONAL));
    }

    private void encodeQuestions(DnsQuery query, ByteBuf buf) throws Exception {
        int count = query.count(DnsSection.QUESTION);
        for (int i = 0; i < count; i++) {
            this.recordEncoder.encodeQuestion((DnsQuestion) query.recordAt(DnsSection.QUESTION, i), buf);
        }
    }

    private void encodeRecords(DnsQuery query, DnsSection section, ByteBuf buf) throws Exception {
        int count = query.count(section);
        for (int i = 0; i < count; i++) {
            this.recordEncoder.encodeRecord(query.recordAt(section, i), buf);
        }
    }
}
