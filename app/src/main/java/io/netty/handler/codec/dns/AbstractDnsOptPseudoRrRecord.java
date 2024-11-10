package io.netty.handler.codec.dns;

import io.netty.handler.codec.http2.Http2CodecUtil;
import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public abstract class AbstractDnsOptPseudoRrRecord extends AbstractDnsRecord implements DnsOptPseudoRecord {
    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractDnsOptPseudoRrRecord(int maxPayloadSize, int extendedRcode, int version) {
        super("", DnsRecordType.OPT, maxPayloadSize, packIntoLong(extendedRcode, version));
    }

    protected AbstractDnsOptPseudoRrRecord(int maxPayloadSize) {
        super("", DnsRecordType.OPT, maxPayloadSize, 0L);
    }

    private static long packIntoLong(int val, int val2) {
        return (((val & 255) << 24) | ((val2 & 255) << 16)) & 4294967295L;
    }

    @Override // io.netty.handler.codec.dns.DnsOptPseudoRecord
    public int extendedRcode() {
        return (short) ((((int) timeToLive()) >> 24) & 255);
    }

    @Override // io.netty.handler.codec.dns.DnsOptPseudoRecord
    public int version() {
        return (short) ((((int) timeToLive()) >> 16) & 255);
    }

    @Override // io.netty.handler.codec.dns.DnsOptPseudoRecord
    public int flags() {
        return (short) (((short) timeToLive()) & Http2CodecUtil.MAX_UNSIGNED_BYTE);
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsRecord
    public String toString() {
        return toStringBuilder().toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final StringBuilder toStringBuilder() {
        return new StringBuilder(64).append(StringUtil.simpleClassName(this)).append('(').append("OPT flags:").append(flags()).append(" version:").append(version()).append(" extendedRecode:").append(extendedRcode()).append(" udp:").append(dnsClass()).append(')');
    }
}
