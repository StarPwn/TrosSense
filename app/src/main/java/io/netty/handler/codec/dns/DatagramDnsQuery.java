package io.netty.handler.codec.dns;

import io.netty.channel.AddressedEnvelope;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/* loaded from: classes4.dex */
public class DatagramDnsQuery extends DefaultDnsQuery implements AddressedEnvelope<DatagramDnsQuery, InetSocketAddress> {
    private final InetSocketAddress recipient;
    private final InetSocketAddress sender;

    public DatagramDnsQuery(InetSocketAddress sender, InetSocketAddress recipient, int id) {
        this(sender, recipient, id, DnsOpCode.QUERY);
    }

    public DatagramDnsQuery(InetSocketAddress sender, InetSocketAddress recipient, int id, DnsOpCode opCode) {
        super(id, opCode);
        if (recipient == null && sender == null) {
            throw new NullPointerException("recipient and sender");
        }
        this.sender = sender;
        this.recipient = recipient;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.channel.AddressedEnvelope
    public DatagramDnsQuery content() {
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.channel.AddressedEnvelope
    public InetSocketAddress sender() {
        return this.sender;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.channel.AddressedEnvelope
    public InetSocketAddress recipient() {
        return this.recipient;
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsQuery, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DatagramDnsQuery setId(int id) {
        return (DatagramDnsQuery) super.setId(id);
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsQuery, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DatagramDnsQuery setOpCode(DnsOpCode opCode) {
        return (DatagramDnsQuery) super.setOpCode(opCode);
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsQuery, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DatagramDnsQuery setRecursionDesired(boolean recursionDesired) {
        return (DatagramDnsQuery) super.setRecursionDesired(recursionDesired);
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsQuery, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DatagramDnsQuery setZ(int z) {
        return (DatagramDnsQuery) super.setZ(z);
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsQuery, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DatagramDnsQuery setRecord(DnsSection section, DnsRecord record) {
        return (DatagramDnsQuery) super.setRecord(section, record);
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsQuery, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DatagramDnsQuery addRecord(DnsSection section, DnsRecord record) {
        return (DatagramDnsQuery) super.addRecord(section, record);
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsQuery, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DatagramDnsQuery addRecord(DnsSection section, int index, DnsRecord record) {
        return (DatagramDnsQuery) super.addRecord(section, index, record);
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsQuery, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DatagramDnsQuery clear(DnsSection section) {
        return (DatagramDnsQuery) super.clear(section);
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsQuery, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DatagramDnsQuery clear() {
        return (DatagramDnsQuery) super.clear();
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsQuery, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public DatagramDnsQuery touch() {
        return (DatagramDnsQuery) super.touch();
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsQuery, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.util.ReferenceCounted
    public DatagramDnsQuery touch(Object hint) {
        return (DatagramDnsQuery) super.touch(hint);
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsQuery, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public DatagramDnsQuery retain() {
        return (DatagramDnsQuery) super.retain();
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsQuery, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public DatagramDnsQuery retain(int increment) {
        return (DatagramDnsQuery) super.retain(increment);
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || !(obj instanceof AddressedEnvelope)) {
            return false;
        }
        AddressedEnvelope<?, SocketAddress> that = (AddressedEnvelope) obj;
        if (sender() == null) {
            if (that.sender() != null) {
                return false;
            }
        } else if (!sender().equals(that.sender())) {
            return false;
        }
        if (recipient() == null) {
            if (that.recipient() != null) {
                return false;
            }
        } else if (!recipient().equals(that.recipient())) {
            return false;
        }
        return true;
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage
    public int hashCode() {
        int hashCode = super.hashCode();
        if (sender() != null) {
            hashCode = (hashCode * 31) + sender().hashCode();
        }
        if (recipient() != null) {
            return (hashCode * 31) + recipient().hashCode();
        }
        return hashCode;
    }
}
