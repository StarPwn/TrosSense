package io.netty.handler.codec.dns;

import io.netty.channel.socket.InternetProtocolFamily;
import java.util.Arrays;

/* loaded from: classes4.dex */
public final class DefaultDnsOptEcsRecord extends AbstractDnsOptPseudoRrRecord implements DnsOptEcsRecord {
    private final byte[] address;
    private final int srcPrefixLength;

    public DefaultDnsOptEcsRecord(int maxPayloadSize, int extendedRcode, int version, int srcPrefixLength, byte[] address) {
        super(maxPayloadSize, extendedRcode, version);
        this.srcPrefixLength = srcPrefixLength;
        this.address = (byte[]) verifyAddress(address).clone();
    }

    public DefaultDnsOptEcsRecord(int maxPayloadSize, int srcPrefixLength, byte[] address) {
        this(maxPayloadSize, 0, 0, srcPrefixLength, address);
    }

    public DefaultDnsOptEcsRecord(int maxPayloadSize, InternetProtocolFamily protocolFamily) {
        this(maxPayloadSize, 0, 0, 0, protocolFamily.localhost().getAddress());
    }

    private static byte[] verifyAddress(byte[] bytes) {
        if (bytes.length == 4 || bytes.length == 16) {
            return bytes;
        }
        throw new IllegalArgumentException("bytes.length must either 4 or 16");
    }

    @Override // io.netty.handler.codec.dns.DnsOptEcsRecord
    public int sourcePrefixLength() {
        return this.srcPrefixLength;
    }

    @Override // io.netty.handler.codec.dns.DnsOptEcsRecord
    public int scopePrefixLength() {
        return 0;
    }

    @Override // io.netty.handler.codec.dns.DnsOptEcsRecord
    public byte[] address() {
        return (byte[]) this.address.clone();
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsOptPseudoRrRecord, io.netty.handler.codec.dns.AbstractDnsRecord
    public String toString() {
        StringBuilder sb = toStringBuilder();
        sb.setLength(sb.length() - 1);
        return sb.append(" address:").append(Arrays.toString(this.address)).append(" sourcePrefixLength:").append(sourcePrefixLength()).append(" scopePrefixLength:").append(scopePrefixLength()).append(')').toString();
    }
}
