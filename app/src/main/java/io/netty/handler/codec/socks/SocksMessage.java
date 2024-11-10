package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public abstract class SocksMessage {
    private final SocksProtocolVersion protocolVersion = SocksProtocolVersion.SOCKS5;
    private final SocksMessageType type;

    @Deprecated
    public abstract void encodeAsByteBuf(ByteBuf byteBuf);

    /* JADX INFO: Access modifiers changed from: protected */
    public SocksMessage(SocksMessageType type) {
        this.type = (SocksMessageType) ObjectUtil.checkNotNull(type, "type");
    }

    public SocksMessageType type() {
        return this.type;
    }

    public SocksProtocolVersion protocolVersion() {
        return this.protocolVersion;
    }
}
