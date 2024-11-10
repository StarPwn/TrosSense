package io.netty.handler.codec.haproxy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.haproxy.HAProxyTLV;
import io.netty.util.internal.StringUtil;
import java.util.Collections;
import java.util.List;

/* loaded from: classes4.dex */
public final class HAProxySSLTLV extends HAProxyTLV {
    private final byte clientBitField;
    private final List<HAProxyTLV> tlvs;
    private final int verify;

    public HAProxySSLTLV(int verify, byte clientBitField, List<HAProxyTLV> tlvs) {
        this(verify, clientBitField, tlvs, Unpooled.EMPTY_BUFFER);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HAProxySSLTLV(int verify, byte clientBitField, List<HAProxyTLV> tlvs, ByteBuf rawContent) {
        super(HAProxyTLV.Type.PP2_TYPE_SSL, (byte) 32, rawContent);
        this.verify = verify;
        this.tlvs = Collections.unmodifiableList(tlvs);
        this.clientBitField = clientBitField;
    }

    public boolean isPP2ClientCertConn() {
        return (this.clientBitField & 2) != 0;
    }

    public boolean isPP2ClientSSL() {
        return (this.clientBitField & 1) != 0;
    }

    public boolean isPP2ClientCertSess() {
        return (this.clientBitField & 4) != 0;
    }

    public byte client() {
        return this.clientBitField;
    }

    public int verify() {
        return this.verify;
    }

    public List<HAProxyTLV> encapsulatedTLVs() {
        return this.tlvs;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // io.netty.handler.codec.haproxy.HAProxyTLV
    public int contentNumBytes() {
        int tlvNumBytes = 0;
        for (int i = 0; i < this.tlvs.size(); i++) {
            tlvNumBytes += this.tlvs.get(i).totalNumBytes();
        }
        int i2 = tlvNumBytes + 5;
        return i2;
    }

    @Override // io.netty.handler.codec.haproxy.HAProxyTLV, io.netty.buffer.DefaultByteBufHolder
    public String toString() {
        return StringUtil.simpleClassName(this) + "(type: " + type() + ", typeByteValue: " + ((int) typeByteValue()) + ", client: " + ((int) client()) + ", verify: " + verify() + ", numEncapsulatedTlvs: " + this.tlvs.size() + ')';
    }
}
