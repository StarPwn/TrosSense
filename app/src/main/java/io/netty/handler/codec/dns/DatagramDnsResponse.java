package io.netty.handler.codec.dns;

import io.netty.channel.AddressedEnvelope;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/* loaded from: classes4.dex */
public class DatagramDnsResponse extends DefaultDnsResponse implements AddressedEnvelope<DatagramDnsResponse, InetSocketAddress> {
    private final InetSocketAddress recipient;
    private final InetSocketAddress sender;

    public DatagramDnsResponse(InetSocketAddress sender, InetSocketAddress recipient, int id) {
        this(sender, recipient, id, DnsOpCode.QUERY, DnsResponseCode.NOERROR);
    }

    public DatagramDnsResponse(InetSocketAddress sender, InetSocketAddress recipient, int id, DnsOpCode opCode) {
        this(sender, recipient, id, opCode, DnsResponseCode.NOERROR);
    }

    public DatagramDnsResponse(InetSocketAddress sender, InetSocketAddress recipient, int id, DnsOpCode opCode, DnsResponseCode responseCode) {
        super(id, opCode, responseCode);
        if (recipient == null && sender == null) {
            throw new NullPointerException("recipient and sender");
        }
        this.sender = sender;
        this.recipient = recipient;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.channel.AddressedEnvelope
    public DatagramDnsResponse content() {
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

    @Override // io.netty.handler.codec.dns.DefaultDnsResponse, io.netty.handler.codec.dns.DnsResponse
    public DatagramDnsResponse setAuthoritativeAnswer(boolean authoritativeAnswer) {
        return (DatagramDnsResponse) super.setAuthoritativeAnswer(authoritativeAnswer);
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsResponse, io.netty.handler.codec.dns.DnsResponse
    public DatagramDnsResponse setTruncated(boolean truncated) {
        return (DatagramDnsResponse) super.setTruncated(truncated);
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsResponse, io.netty.handler.codec.dns.DnsResponse
    public DatagramDnsResponse setRecursionAvailable(boolean recursionAvailable) {
        return (DatagramDnsResponse) super.setRecursionAvailable(recursionAvailable);
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsResponse, io.netty.handler.codec.dns.DnsResponse
    public DatagramDnsResponse setCode(DnsResponseCode code) {
        return (DatagramDnsResponse) super.setCode(code);
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsResponse, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DatagramDnsResponse setId(int id) {
        return (DatagramDnsResponse) super.setId(id);
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsResponse, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DatagramDnsResponse setOpCode(DnsOpCode opCode) {
        return (DatagramDnsResponse) super.setOpCode(opCode);
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsResponse, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DatagramDnsResponse setRecursionDesired(boolean recursionDesired) {
        return (DatagramDnsResponse) super.setRecursionDesired(recursionDesired);
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsResponse, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DatagramDnsResponse setZ(int z) {
        return (DatagramDnsResponse) super.setZ(z);
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsResponse, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DatagramDnsResponse setRecord(DnsSection section, DnsRecord record) {
        return (DatagramDnsResponse) super.setRecord(section, record);
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsResponse, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DatagramDnsResponse addRecord(DnsSection section, DnsRecord record) {
        return (DatagramDnsResponse) super.addRecord(section, record);
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsResponse, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DatagramDnsResponse addRecord(DnsSection section, int index, DnsRecord record) {
        return (DatagramDnsResponse) super.addRecord(section, index, record);
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsResponse, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DatagramDnsResponse clear(DnsSection section) {
        return (DatagramDnsResponse) super.clear(section);
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsResponse, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DatagramDnsResponse clear() {
        return (DatagramDnsResponse) super.clear();
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsResponse, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public DatagramDnsResponse touch() {
        return (DatagramDnsResponse) super.touch();
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsResponse, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.util.ReferenceCounted
    public DatagramDnsResponse touch(Object hint) {
        return (DatagramDnsResponse) super.touch(hint);
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsResponse, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public DatagramDnsResponse retain() {
        return (DatagramDnsResponse) super.retain();
    }

    @Override // io.netty.handler.codec.dns.DefaultDnsResponse, io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public DatagramDnsResponse retain(int increment) {
        return (DatagramDnsResponse) super.retain(increment);
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
            return that.recipient() == null;
        }
        return recipient().equals(that.recipient());
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
