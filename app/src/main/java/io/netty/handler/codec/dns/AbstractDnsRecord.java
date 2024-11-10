package io.netty.handler.codec.dns;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.net.IDN;
import kotlin.UShort;

/* loaded from: classes4.dex */
public abstract class AbstractDnsRecord implements DnsRecord {
    private final short dnsClass;
    private int hashCode;
    private final String name;
    private final long timeToLive;
    private final DnsRecordType type;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractDnsRecord(String name, DnsRecordType type, long timeToLive) {
        this(name, type, 1, timeToLive);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractDnsRecord(String name, DnsRecordType type, int dnsClass, long timeToLive) {
        ObjectUtil.checkPositiveOrZero(timeToLive, "timeToLive");
        this.name = appendTrailingDot(IDNtoASCII(name));
        this.type = (DnsRecordType) ObjectUtil.checkNotNull(type, "type");
        this.dnsClass = (short) dnsClass;
        this.timeToLive = timeToLive;
    }

    private static String IDNtoASCII(String name) {
        ObjectUtil.checkNotNull(name, "name");
        if (PlatformDependent.isAndroid() && ".".equals(name)) {
            return name;
        }
        return IDN.toASCII(name);
    }

    private static String appendTrailingDot(String name) {
        if (name.length() > 0 && name.charAt(name.length() - 1) != '.') {
            return name + '.';
        }
        return name;
    }

    @Override // io.netty.handler.codec.dns.DnsRecord
    public String name() {
        return this.name;
    }

    @Override // io.netty.handler.codec.dns.DnsRecord
    public DnsRecordType type() {
        return this.type;
    }

    @Override // io.netty.handler.codec.dns.DnsRecord
    public int dnsClass() {
        return this.dnsClass & UShort.MAX_VALUE;
    }

    @Override // io.netty.handler.codec.dns.DnsRecord
    public long timeToLive() {
        return this.timeToLive;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DnsRecord)) {
            return false;
        }
        DnsRecord that = (DnsRecord) obj;
        int hashCode = this.hashCode;
        if (hashCode == 0 || hashCode == that.hashCode()) {
            return type().intValue() == that.type().intValue() && dnsClass() == that.dnsClass() && name().equals(that.name());
        }
        return false;
    }

    public int hashCode() {
        int hashCode = this.hashCode;
        if (hashCode != 0) {
            return hashCode;
        }
        int hashCode2 = (this.name.hashCode() * 31) + (type().intValue() * 31) + dnsClass();
        this.hashCode = hashCode2;
        return hashCode2;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder(64);
        buf.append(StringUtil.simpleClassName(this)).append('(').append(name()).append(' ').append(timeToLive()).append(' ');
        DnsMessageUtil.appendRecordClass(buf, dnsClass()).append(' ').append(type().name()).append(')');
        return buf.toString();
    }
}
