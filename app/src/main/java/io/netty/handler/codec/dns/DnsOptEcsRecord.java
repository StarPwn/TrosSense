package io.netty.handler.codec.dns;

/* loaded from: classes4.dex */
public interface DnsOptEcsRecord extends DnsOptPseudoRecord {
    byte[] address();

    int scopePrefixLength();

    int sourcePrefixLength();
}
