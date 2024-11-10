package io.netty.handler.codec.dns;

/* loaded from: classes4.dex */
public class DefaultDnsQuery extends AbstractDnsMessage implements DnsQuery {
    public DefaultDnsQuery(int id) {
        super(id);
    }

    public DefaultDnsQuery(int id, DnsOpCode opCode) {
        super(id, opCode);
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DnsQuery setId(int id) {
        return (DnsQuery) super.setId(id);
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DnsQuery setOpCode(DnsOpCode opCode) {
        return (DnsQuery) super.setOpCode(opCode);
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DnsQuery setRecursionDesired(boolean recursionDesired) {
        return (DnsQuery) super.setRecursionDesired(recursionDesired);
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DnsQuery setZ(int z) {
        return (DnsQuery) super.setZ(z);
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DnsQuery setRecord(DnsSection section, DnsRecord record) {
        return (DnsQuery) super.setRecord(section, record);
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DnsQuery addRecord(DnsSection section, DnsRecord record) {
        return (DnsQuery) super.addRecord(section, record);
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DnsQuery addRecord(DnsSection section, int index, DnsRecord record) {
        return (DnsQuery) super.addRecord(section, index, record);
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DnsQuery clear(DnsSection section) {
        return (DnsQuery) super.clear(section);
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DnsQuery clear() {
        return (DnsQuery) super.clear();
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public DnsQuery touch() {
        return (DnsQuery) super.touch();
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.util.ReferenceCounted
    public DnsQuery touch(Object hint) {
        return (DnsQuery) super.touch(hint);
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public DnsQuery retain() {
        return (DnsQuery) super.retain();
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public DnsQuery retain(int increment) {
        return (DnsQuery) super.retain(increment);
    }

    public String toString() {
        return DnsMessageUtil.appendQuery(new StringBuilder(128), this).toString();
    }
}
