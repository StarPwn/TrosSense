package io.netty.handler.codec.dns;

import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public class DefaultDnsResponse extends AbstractDnsMessage implements DnsResponse {
    private boolean authoritativeAnswer;
    private DnsResponseCode code;
    private boolean recursionAvailable;
    private boolean truncated;

    public DefaultDnsResponse(int id) {
        this(id, DnsOpCode.QUERY, DnsResponseCode.NOERROR);
    }

    public DefaultDnsResponse(int id, DnsOpCode opCode) {
        this(id, opCode, DnsResponseCode.NOERROR);
    }

    public DefaultDnsResponse(int id, DnsOpCode opCode, DnsResponseCode code) {
        super(id, opCode);
        setCode(code);
    }

    @Override // io.netty.handler.codec.dns.DnsResponse
    public boolean isAuthoritativeAnswer() {
        return this.authoritativeAnswer;
    }

    public DnsResponse setAuthoritativeAnswer(boolean authoritativeAnswer) {
        this.authoritativeAnswer = authoritativeAnswer;
        return this;
    }

    @Override // io.netty.handler.codec.dns.DnsResponse
    public boolean isTruncated() {
        return this.truncated;
    }

    public DnsResponse setTruncated(boolean truncated) {
        this.truncated = truncated;
        return this;
    }

    @Override // io.netty.handler.codec.dns.DnsResponse
    public boolean isRecursionAvailable() {
        return this.recursionAvailable;
    }

    public DnsResponse setRecursionAvailable(boolean recursionAvailable) {
        this.recursionAvailable = recursionAvailable;
        return this;
    }

    @Override // io.netty.handler.codec.dns.DnsResponse
    public DnsResponseCode code() {
        return this.code;
    }

    public DnsResponse setCode(DnsResponseCode code) {
        this.code = (DnsResponseCode) ObjectUtil.checkNotNull(code, "code");
        return this;
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DnsResponse setId(int id) {
        return (DnsResponse) super.setId(id);
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DnsResponse setOpCode(DnsOpCode opCode) {
        return (DnsResponse) super.setOpCode(opCode);
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DnsResponse setRecursionDesired(boolean recursionDesired) {
        return (DnsResponse) super.setRecursionDesired(recursionDesired);
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DnsResponse setZ(int z) {
        return (DnsResponse) super.setZ(z);
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DnsResponse setRecord(DnsSection section, DnsRecord record) {
        return (DnsResponse) super.setRecord(section, record);
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DnsResponse addRecord(DnsSection section, DnsRecord record) {
        return (DnsResponse) super.addRecord(section, record);
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DnsResponse addRecord(DnsSection section, int index, DnsRecord record) {
        return (DnsResponse) super.addRecord(section, index, record);
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DnsResponse clear(DnsSection section) {
        return (DnsResponse) super.clear(section);
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.handler.codec.dns.DnsMessage
    public DnsResponse clear() {
        return (DnsResponse) super.clear();
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public DnsResponse touch() {
        return (DnsResponse) super.touch();
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.util.ReferenceCounted
    public DnsResponse touch(Object hint) {
        return (DnsResponse) super.touch(hint);
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public DnsResponse retain() {
        return (DnsResponse) super.retain();
    }

    @Override // io.netty.handler.codec.dns.AbstractDnsMessage, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public DnsResponse retain(int increment) {
        return (DnsResponse) super.retain(increment);
    }

    public String toString() {
        return DnsMessageUtil.appendResponse(new StringBuilder(128), this).toString();
    }
}
