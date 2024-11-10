package io.netty.handler.codec.dns;

import io.netty.buffer.ByteBuf;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public class DefaultDnsRawRecord extends AbstractDnsRecord implements DnsRawRecord {
    private final ByteBuf content;

    public DefaultDnsRawRecord(String name, DnsRecordType type, long timeToLive, ByteBuf content) {
        this(name, type, 1, timeToLive, content);
    }

    public DefaultDnsRawRecord(String name, DnsRecordType type, int dnsClass, long timeToLive, ByteBuf content) {
        super(name, type, dnsClass, timeToLive);
        this.content = (ByteBuf) ObjectUtil.checkNotNull(content, "content");
    }

    @Override // io.netty.buffer.ByteBufHolder
    public ByteBuf content() {
        return this.content;
    }

    @Override // io.netty.buffer.ByteBufHolder
    public DnsRawRecord copy() {
        return replace(content().copy());
    }

    @Override // io.netty.buffer.ByteBufHolder
    public DnsRawRecord duplicate() {
        return replace(content().duplicate());
    }

    @Override // io.netty.buffer.ByteBufHolder
    public DnsRawRecord retainedDuplicate() {
        return replace(content().retainedDuplicate());
    }

    @Override // io.netty.buffer.ByteBufHolder
    public DnsRawRecord replace(ByteBuf content) {
        return new DefaultDnsRawRecord(name(), type(), dnsClass(), timeToLive(), content);
    }

    @Override // io.netty.util.ReferenceCounted
    public int refCnt() {
        return content().refCnt();
    }

    @Override // io.netty.util.ReferenceCounted
    public DnsRawRecord retain() {
        content().retain();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public DnsRawRecord retain(int increment) {
        content().retain(increment);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release() {
        return content().release();
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release(int decrement) {
        return content().release(decrement);
    }

    @Override // io.netty.util.ReferenceCounted
    public DnsRawRecord touch() {
        content().touch();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public DnsRawRecord touch(Object hint) {
        content().touch(hint);
        return this;
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsRecord
    public String toString() {
        StringBuilder buf = new StringBuilder(64).append(StringUtil.simpleClassName(this)).append('(');
        DnsRecordType type = type();
        if (type != DnsRecordType.OPT) {
            buf.append(name().isEmpty() ? "<root>" : name()).append(' ').append(timeToLive()).append(' ');
            DnsMessageUtil.appendRecordClass(buf, dnsClass()).append(' ').append(type.name());
        } else {
            buf.append("OPT flags:").append(timeToLive()).append(" udp:").append(dnsClass());
        }
        buf.append(' ').append(content().readableBytes()).append("B)");
        return buf.toString();
    }
}
